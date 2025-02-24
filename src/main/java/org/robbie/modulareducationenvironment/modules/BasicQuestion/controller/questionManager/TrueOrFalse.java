package org.robbie.modulareducationenvironment.modules.BasicQuestion.controller.questionManager;

import org.robbie.modulareducationenvironment.settings.dataTypes.Tuple;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrueOrFalse extends BasicQuestion{
    public static Map<String, Object> convertQuestionSettings(ValueHolder questionSettings) throws IllegalArgumentException {
        if(questionSettings.getType() != SettingType.Group){
            throw new IllegalArgumentException("True or False holder must be a group");
        }
        Map<String, Object> convertedSettings = new HashMap<>();
        convertedSettings.put("type", "TrueOrFalse");

        Map<String, ValueHolder> allFillInSettings = (Map<String, ValueHolder>) questionSettings.getValue();
        Tuple<String, String> questionAndDescription = getQuestionAndDescription(allFillInSettings);
        convertedSettings.put("question", questionAndDescription.getFirst());
        convertedSettings.put("description", questionAndDescription.getSecond());

        if(!allFillInSettings.containsKey("Answer")){
            throw new IllegalArgumentException("True or False setting must have whether answer is true or false");
        }
        Boolean answer = (Boolean) allFillInSettings.get("Answer").getValue();
        convertedSettings.put("answer", answer);

        convertedSettings.put("id", UUID.randomUUID().toString());

        return convertedSettings;
    }
}
