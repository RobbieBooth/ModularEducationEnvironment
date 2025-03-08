package org.robbie.modulareducationenvironment.modules.AIMultiChoice.generation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.JsonValue;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import com.openai.models.ResponseFormatJsonSchema;
import org.robbie.modulareducationenvironment.modules.AIMultiChoice.generation.questionManager.MultipleChoiceQuestion;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateQuestion {

    public static List<MultipleChoiceQuestion> generateMultipleChoiceQuestions(Integer questionCount, String codeContext, String questionTopics, String codeTemplate, String codeLanguage, String studentCode, String open_api_key){
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(open_api_key)
                .build();

        ResponseFormatJsonSchema.JsonSchema.Schema schema = ResponseFormatJsonSchema.JsonSchema.Schema.builder()
                .putAdditionalProperty("type", JsonValue.from("object")) // Change top-level type to "object"
                .putAdditionalProperty("properties", JsonValue.from(Map.of(
                        "questions", Map.of( // Define "questions" as an array inside an object
                                "type", "array",
                                "items", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "question", Map.of("type", "string"),
                                                "options", Map.of(
                                                        "type", "array",
                                                        "items", Map.of(
                                                                "type", "object",
                                                                "properties", Map.of(
                                                                        "answer", Map.of("type", "boolean"),
                                                                        "option", Map.of("type", "string")
                                                                ),
                                                                "required", List.of("answer", "option")
                                                        )
                                                )
                                        ),
                                        "required", List.of("question", "options")
                                )
                        )
                )))
                .build();

        //Creates a schema of our AI Module output:
        //[{
        //"question": String,
        //"options": [{
        //      "answer":Bool,
        //      "option":string,
        //  }]
        //}]

        String userString = "{}";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Create a JSON object
//            Map<String, Object> userJson = Map.of(
//                    "question_count", questionCount,
//                    "question_topics", questionTopics,
//                    "code_context", codeContext,
//                    "code_template", codeTemplate,
//                    "student_code", studentCode,
//                    "code_language", codeLanguage
//            );
            Map<String, Object> userJson = new HashMap<>();
            userJson.put("question_count", questionCount);
            userJson.put("question_topics", questionTopics);
            userJson.put("code_context", codeContext);
            userJson.put("code_template", codeTemplate);
            userJson.put("student_code", studentCode);
            userJson.put("code_language", codeLanguage);


            // Convert to JSON string
            userString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userJson);
        } catch (IOException e) {
            e.printStackTrace();
            userString = "{}";  // Return empty JSON in case of an error
        }

        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4O_MINI)
                .maxCompletionTokens(2048)
                .responseFormat(ResponseFormatJsonSchema.builder()
                        .jsonSchema(ResponseFormatJsonSchema.JsonSchema.builder()
                                .name("questions")
                                .schema(schema)
                                .build())
                        .build())
                .addSystemMessage(
                        "You are an educational assistant specializing in computer science. Your task is to analyse " +
                        "students' code for the beginner programmer class and generate thoughtful multiple-choice questions that " +
                        "can help them understand and improve their coding skills. You should try and make good distractor " +
                        "options to really test students understanding.\n" +
                        "\n" +
                        "The student will provide a json with the following:\n" +
                        "How many questions they want under \"question_count\",\n" +
                        "Which topics they want to be assessed on by \"question_topics\",\n" +
                        "\"code_context\" telling you details about their code,\n" +
                        "You should only assess students on code that has been modified from \"code_template\",\n" +
                        "\"student_code\" is the students code that has been modified,\n" +
                        "\"code_language\" is the language that the student has used\n" +
                        "\n" +
                        "The questions should have 4 options.\n" +
                        "\n" +
                        "You should create questions using the topics that you believe are appropriate for the students’ code given.\n")
                .addUserMessage(userString)
                .build();

        ChatCompletion response = client.chat().completions().create(createParams);

        // Capture the JSON response from OpenAI
        String jsonResponse = response.choices().stream()
                .flatMap(choice -> choice.message().content().stream())
                .findFirst() // Get first message content
                .orElse(""); // Default to empty string if no content

        // Parse and convert JSON to List<MultipleChoiceQuestion>
        return parseJsonToQuestions(jsonResponse);

    }

    private static List<MultipleChoiceQuestion> parseJsonToQuestions(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse JSON as a tree structure
            JsonNode rootNode = objectMapper.readTree(json);

            // Extract questions array from the root JSON object
            JsonNode questionsNode = rootNode.get("questions");

            if (questionsNode != null && questionsNode.isArray()) {
                // Convert JSON array into List<MultipleChoiceQuestion>
                return objectMapper.readValue(questionsNode.toString(),
                        new TypeReference<List<MultipleChoiceQuestion>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of(); // Return empty list if parsing fails
    }

}
