package org.robbie.modulareducationenvironment.modules.AIMultiChoice.factory;

import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.factory.AbstractQuestionFactory;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleLoader;
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
    final String MODULE_NAME = "AIMultiChoice";
    private static final Logger logger = LoggerFactory.getLogger(QuestionFactory.class);
    private static final Integer DEFAULT_QUESTION_COUNT = 4;
    private static final String DEFAULT_QUESTION_TYPE = "Multiple Choice";
    private static final String DEFAULT_TOPICS_ASSESSED = "Core Coding Concepts";


    @Override
    public String createPage(QuestionState state) {
        return "index.html";
    }

    @Override
    public BaseSetting getDefaultQuestionSettings(Map<String, String> globalSettings) {
        try{
            System.out.println(ModuleLoader.getModuleResourcePath(MODULE_NAME) + "defaultQuestionSettings.json");

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

        ValueHolder allSettingsHolder = baseSetting.getValueHolder();
        if(allSettingsHolder.getType() != SettingType.Group){
            return Map.of();
        }
        Map<String, ValueHolder> allSettingsBefore = (Map<String, ValueHolder>) allSettingsHolder.getValue();
        String linkedQuestionID;
        String numberOfQuestions;
        String questionType;
        String topicsAssessed;
        String codeLanguage;
        String codeContext;
        String codeTemplate;
        try{
            linkedQuestionID = getStringFromSetting(allSettingsBefore.get("Linked Question ID"), "Linked Question ID");
            numberOfQuestions = getStringFromSetting(allSettingsBefore.get("Number of Questions"), "Number of Questions");
            //Because Question Type is a select the logic is different from a normal input.
            ValueHolder questionTypeHolder = allSettingsBefore.get("Question Type");
            if(questionTypeHolder == null){
                //Will be caught by catch below
                logger.error("Question Type is not defined when parsing the settings");
                throw new IllegalArgumentException("Question Type Setting holder is not defined");
            }
            List<String> typesSelected = (List<String>) questionTypeHolder.getValue();
            if(typesSelected.size() == 0){
                //this gets updated to default later on
                questionType = null;
            }else{
                questionType = typesSelected.get(0);
            }

//            questionType = getStringFromSetting(allSettingsBefore.get("Question Type"), "Question Type");
            topicsAssessed = getStringFromSetting(allSettingsBefore.get("Topics Assessed"), "Topics Assessed");
            codeLanguage = getStringFromSetting(allSettingsBefore.get("Code Language"), "Code Language");
            codeContext = getStringFromSetting(allSettingsBefore.get("Code Context"), "Code Context");
            codeTemplate = getStringFromSetting(allSettingsBefore.get("Code Template"), "Code Template");
        }catch(Exception e){
            throw new RuntimeException("Error parsing default settings for "+MODULE_NAME,e);
        }
        if(!isCompulsoryValueSet(linkedQuestionID)){
            String errorMessage = "Error parsing default settings for "+MODULE_NAME + ": Linked Question ID must be defined";
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        //Parse Question count setting
        Integer questionCount = DEFAULT_QUESTION_COUNT;
        if(!isCompulsoryValueSet(numberOfQuestions)){
            logger.warn("Number of questions is empty so default of `{}` is used!", DEFAULT_QUESTION_COUNT);
        }else{
            try {
                questionCount = Integer.parseInt(numberOfQuestions);
                if(questionCount <= 1){
                    questionCount = DEFAULT_QUESTION_COUNT;
                    logger.warn("Number of questions specified to generate must be non negative number and more than 0, so default question count of `{}` is used!", DEFAULT_QUESTION_COUNT);
                }
            } catch (NumberFormatException e1) {
                logger.warn("Number of questions is not a valid number so default of `{}` is used!", DEFAULT_QUESTION_COUNT);
                questionCount = DEFAULT_QUESTION_COUNT;
            }
        }

        //Parse question type setting
        if(!isCompulsoryValueSet(questionType)){
            logger.warn("Question type is empty so default type of `{}` is used!", DEFAULT_QUESTION_TYPE);
            questionType = DEFAULT_QUESTION_TYPE;
        }

        //Parse topics to assess
        if(!isCompulsoryValueSet(topicsAssessed)){
            logger.warn("Topics Assessed is empty so default type of `{}` is used!", DEFAULT_TOPICS_ASSESSED);
            topicsAssessed = DEFAULT_TOPICS_ASSESSED;
        }

        //The rest don't need parsed as they can be empty or null
        //Put settings in map
        settings.put("linkedQuestionID", linkedQuestionID);
        settings.put("numberOfQuestions", questionCount);
        settings.put("questionType", questionType);
        settings.put("topicsAssessed", topicsAssessed);
        settings.put("codeLanguage", codeLanguage);
        settings.put("codeContext", codeContext);
        settings.put("codeTemplate", codeTemplate);
        settings.put("questions", new ArrayList<>());

        return settings;
    }

    /**
     * Used to check that the string is not null and is non-empty string
     * @param value
     * @return
     */
    private boolean isCompulsoryValueSet(String value){
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Used to get the string from the value holder of Input type
     * @param valueHolder
     * @param settingName
     * @return
     * @throws IllegalArgumentException
     */
    private String getStringFromSetting(ValueHolder valueHolder, String settingName) throws IllegalArgumentException{
        if(valueHolder == null){
            String errorMessage = settingName + "is not defined in settings";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        String linkedQuestionID = null;
        try{
            linkedQuestionID = getValueFromValueHolder(valueHolder);
        } catch (IllegalArgumentException e) {
            String errorMessage = settingName +  "is not of type Input";
            logger.error("{} is not of type Input", settingName);
            throw new IllegalArgumentException(errorMessage);
        }
        return linkedQuestionID;
    }

    @Override
    public Map<String, Object> createAdditionalData(Map<String, String> globalSettings, Map<String, Object> questionSettings) {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("isSubmitted", false);
        additionalData.put("isGenerated", false);
        additionalData.put("receivedCode", false);
        return additionalData;
    }

    /**
     * Gets the input value String from the ValueHolder.
     * If the ValueHolder is not of type `Input` then `IllegalArgumentException` is thrown. Or if value held is not type string null is returned.
     * @param valueHolder the value holder, holding the string from the setting
     * @return The String held in the Value Holder
     */
    private String getValueFromValueHolder(ValueHolder valueHolder) throws IllegalArgumentException{
        if(valueHolder.getType() != SettingType.Input){
            throw new IllegalArgumentException("ValueHolder is not of type Input");
        }
        Object value = valueHolder.getValue();
        if(value instanceof String){
            return (String) value;
        }
        return null;
    }
}
