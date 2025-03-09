package org.robbie.modulareducationenvironment.modules.BasicQuestion.controller.questionManager;

import org.robbie.modulareducationenvironment.settings.dataTypes.Tuple;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Numerical extends BasicQuestion {
    public static Map<String, Object> convertQuestionSettings(ValueHolder questionSettings) throws IllegalArgumentException {
        if(questionSettings.getType() != SettingType.Group){
            throw new IllegalArgumentException("Numerical holder must be a group");
        }
        Map<String, Object> convertedSettings = new HashMap<>();
        convertedSettings.put("type", "Numerical");

        Map<String, ValueHolder> allFillInSettings = (Map<String, ValueHolder>) questionSettings.getValue();
        Tuple<String, String> questionAndDescription = getQuestionAndDescription(allFillInSettings);
        convertedSettings.put("question", questionAndDescription.getFirst());
        convertedSettings.put("description", questionAndDescription.getSecond());

        if(!allFillInSettings.containsKey("Answer")){
            throw new IllegalArgumentException("Numerical setting must have must have an answer");
        }
        String answer = (String) allFillInSettings.get("Answer").getValue();
        //Checking if we can parse as an int otherwise we parse as a double and lastly if we cant parse at all we throw error
        try {
            int intValue = Integer.parseInt(answer);
            convertedSettings.put("answer", intValue);
        } catch (NumberFormatException e1) {
            //If integer parsing fails, we try parsing as a double
            try {
                double doubleValue = Double.parseDouble(answer);
                convertedSettings.put("answer", doubleValue);
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("Invalid number format for numerical question: " + answer);
            }
        }

        convertedSettings.put("id", UUID.randomUUID().toString());

        return convertedSettings;
    }
}
