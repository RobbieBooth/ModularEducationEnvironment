package org.robbie.modulareducationenvironment.modules.BasicQuestion.controller.questionManager;

import org.robbie.modulareducationenvironment.settings.dataTypes.Tuple;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FillInTheBlank extends BasicQuestion {
    public static Map<String, Object> convertQuestionSettings(ValueHolder questionSettings) throws IllegalArgumentException {
        if(questionSettings.getType() != SettingType.Group){
            throw new IllegalArgumentException("Fill in blank holder must be a group");
        }
        Map<String, Object> convertedSettings = new HashMap<>();

        Map<String, ValueHolder> allFillInSettings = (Map<String, ValueHolder>) questionSettings.getValue();
        Tuple<String, String> questionAndDescription = getQuestionAndDescription(allFillInSettings);
        convertedSettings.put("question", questionAndDescription.getFirst());
        convertedSettings.put("description", questionAndDescription.getSecond());

        if(!allFillInSettings.containsKey("Question Text")){
            throw new IllegalArgumentException("Fill in blank must contain question text");
        }
        String questionText = (String) allFillInSettings.get("Question Text").getValue();
        //Make the text into normal text and get all the options.
        ExtractionResult extractionResult = extractBracketContentsAndClean(questionText);

        convertedSettings.put("options", extractionResult.getOptions());
        convertedSettings.put("questionString", extractionResult.getCleanedString());
        return convertedSettings;
    }

    /**
     * This based on the moodle embedded answers. To input an option into the text do this without the quotations: `{1:MULTICHOICE:=California}` - where California is the answer.
     * This will bring up a multi choice box in its place with all of the options you have defined in the question.
     * This is a very basic implementation, so only what was described above is implemented, however it is based on this website here: https://docs.moodle.org/405/en/Embedded_Answers_(Cloze)_question_type
     * @param text The text of which the brackets are removed
     * @return the extracted result with the text without these options brackets and the list of options
     */
    private static ExtractionResult extractBracketContentsAndClean(String text) {
        List<Option> options = new ArrayList<>();
        StringBuilder cleanedText = new StringBuilder();

        Pattern pattern = Pattern.compile("\\{[^:=]*:[^:=]*:=([^}]*)\\}");
        Matcher matcher = pattern.matcher(text);

        int currentIndex = 0; //Keeps track of the index in the cleaned text
        int lastEnd = 0; //Tracks the end of the last match in the original text

        while (matcher.find()) {
            //Append the text before the current match
            cleanedText.append(text, lastEnd, matcher.start());

            //Store extracted word and its corrected index
            String word = matcher.group(1);
            options.add(new Option(cleanedText.length(), word));

            lastEnd = matcher.end(); //Move past the matched brackets

            //Append a space or maintain spacing to ensure readability
            if (cleanedText.length() > 0 && cleanedText.charAt(cleanedText.length() - 1) != ' ') {
                cleanedText.append(" ");
            }
        }

        cleanedText.append(text.substring(lastEnd));

        String finalCleanedText = cleanedText.toString();

        return new ExtractionResult(options, finalCleanedText);
    }

    static class ExtractionResult {
        List<Option> options;
        String cleanedString;

        ExtractionResult(List<Option> options, String cleanedString) {
            this.options = options;
            this.cleanedString = cleanedString;
        }

        public List<Option> getOptions() {
            return options;
        }

        public String getCleanedString() {
            return cleanedString;
        }
    }

    static class Option{
        Integer locationIndex;
        String option;

        public Option(Integer locationIndex, String option) {
            this.locationIndex = locationIndex;
            this.option = option;
        }

        public Integer getLocationIndex() {
            return locationIndex;
        }

        public String getOption() {
            return option;
        }
    }

}
