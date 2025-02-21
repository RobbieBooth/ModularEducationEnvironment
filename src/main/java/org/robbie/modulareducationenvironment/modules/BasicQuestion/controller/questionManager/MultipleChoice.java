package org.robbie.modulareducationenvironment.modules.BasicQuestion.controller.questionManager;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleChoice extends BasicQuestion {

    public static Map<String, Object> convertQuestionSettings(ValueHolder questionSettings) throws IllegalArgumentException {
        try{
            Map<String, Object> questionSettingMap = new HashMap<String, Object>();
            if(questionSettings.getType() != SettingType.Group){
                throw new IllegalArgumentException("Multiple choice question settings should be of format group");
            }
            HashMap<String, ValueHolder> settingNameToValue = (HashMap<String, ValueHolder>) questionSettings.getValue();
            if(!settingNameToValue.containsKey("Description")  || !settingNameToValue.containsKey("Question") || !settingNameToValue.containsKey("Options")){
                throw new IllegalArgumentException("Multiple choice question setting should contain a description, a question and the options");
            }
            String description = (String) settingNameToValue.get("Description").getValue();
            questionSettingMap.put("description", description);

            String question = (String) settingNameToValue.get("Question").getValue();
            questionSettingMap.put("question", question);

            List<ValueHolder> optionHolder = (List<ValueHolder>) settingNameToValue.get("Options").getValue();
            List<OptionHolder> allOptions = new ArrayList<OptionHolder>();
            for(ValueHolder option : optionHolder){
                if(option.getType() != SettingType.Group){
                    throw new IllegalArgumentException("Option type should be of format group");
                }
                Map<String, ValueHolder> fieldNameToValue = (HashMap<String, ValueHolder>) option.getValue();
                allOptions.add(convertOptionSetting(fieldNameToValue));
            }
            questionSettingMap.put("options", allOptions);
            //Convert options
            return questionSettingMap;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static OptionHolder convertOptionSetting(Map<String, ValueHolder> optionSettings) throws IllegalArgumentException {
        if(!optionSettings.containsKey("Option Text") || !optionSettings.containsKey("Answer")){
            throw new IllegalArgumentException("Multiple choice option setting should contain option text and if its an answer");
        }
        String optionText = (String) optionSettings.get("Option Text").getValue();
        Boolean answer = (Boolean) optionSettings.get("Answer").getValue();
        return new OptionHolder(answer, optionText);
    }

    private static class OptionHolder {
        private Boolean answer;
        private String option;
        public OptionHolder(Boolean answer, String option) {
            this.answer = answer;
            this.option = option;
        }

        public OptionHolder() {
        }

        public Boolean answer() {
            return answer;
        }

        public String getOption() {
            return option;
        }
    }
}


