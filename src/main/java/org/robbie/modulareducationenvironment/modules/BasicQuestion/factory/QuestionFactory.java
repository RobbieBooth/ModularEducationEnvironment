package org.robbie.modulareducationenvironment.modules.BasicQuestion.factory;

import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.factory.AbstractQuestionFactory;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleLoader;
import org.robbie.modulareducationenvironment.modules.BasicQuestion.controller.questionManager.*;
import org.robbie.modulareducationenvironment.questionBank.Question;
import org.robbie.modulareducationenvironment.settings.dataTypes.Tuple;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.QuestionSettingReader;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.ErrorSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;


public class QuestionFactory extends AbstractQuestionFactory {
    final String MODULE_NAME = "BasicQuestion";
    private static final Logger logger = LoggerFactory.getLogger(QuestionFactory.class);

    @Override
    public String createPage(QuestionState state) {
        return "index.html";
    }

    @Override
    public BaseSetting getDefaultQuestionSettings(Map<String, String> globalSettings) {
        try{
            ClassPathResource resource = new ClassPathResource("static"+ModuleLoader.getModuleResourcePath(MODULE_NAME) + "defaultQuestionSettings.json");

            return QuestionSettingReader.readSettingJson(resource.getFile());
        } catch (IOException e) {
            return new ErrorSetting(
              "Error",
                    "An error happened getting the quiz settings for "+MODULE_NAME,
                    false,
                    false,
                    "Error loading default settings for "+MODULE_NAME,
                    e.getMessage()
            );
        }

    }

    @Override
    public Map<String, Object> createQuestionSettings(Map<String, String> globalSettings, BaseSetting baseSetting) {
        Map<String, Object> settings = new HashMap<>();
        List<Map<String, Object>> allQuestions = new ArrayList<>();


        ValueHolder allQuestionsHolder = baseSetting.getValueHolder();
        if(allQuestionsHolder.getType() != SettingType.ListSetting){
            return Map.of();
        }
        List<ValueHolder> allQuestionsBefore = (List<ValueHolder>) allQuestionsHolder.getValue();
        for(ValueHolder question : allQuestionsBefore){
            if(question.getType() == SettingType.ConditionalSelect){
                Map<String, ValueHolder> questionSettings = (HashMap<String, ValueHolder>) question.getValue();
                Optional<Map.Entry<String, ValueHolder>> firstEntry = questionSettings.entrySet().stream().findFirst();
                if (firstEntry.isEmpty()) {
                    break;
                }
                try{
                    Map<String, Object> convertedQuestionSetting = convertSettingToQuestion(new Tuple<>(firstEntry.get().getKey(), firstEntry.get().getValue()));
                    allQuestions.add(convertedQuestionSetting);
                }catch(Exception e){
                    logger.error("Error converting the question settings into a Map for type: {}", firstEntry.get().getKey(), e);
                    break;
                }
            }
        }
        settings.put("questions", allQuestions);
        return settings;
    }

    //        "Multiple Choice",
    //        "Fill in the Blank",
    //        "True or False",
    //        "Short Answer",
    //        "Long Answer",
    //        "Numerical"

    private static Map<String, Object> convertSettingToQuestion(Tuple<String, ValueHolder> questionSetting) throws IllegalArgumentException{
        switch (questionSetting.getFirst()){
            case "Multiple Choice":
                return MultipleChoice.convertQuestionSettings(questionSetting.getSecond());
            case "Fill in the Blank":
                return FillInTheBlank.convertQuestionSettings(questionSetting.getSecond());
            case "True or False":
                return TrueOrFalse.convertQuestionSettings(questionSetting.getSecond());
            case "Short Answer":
                return ShortAnswer.convertQuestionSettings(questionSetting.getSecond());
            case "Long Answer":
                return LongAnswer.convertQuestionSettings(questionSetting.getSecond());
            case "Numerical":
                return Numerical.convertQuestionSettings(questionSetting.getSecond());
            default:
                throw new IllegalArgumentException("Question of type: "+questionSetting.getFirst() + " does not exist on basic question module");
        }
    }
}
