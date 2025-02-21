package org.robbie.modulareducationenvironment.modules.BasicQuestion.controller.questionManager;

import org.robbie.modulareducationenvironment.settings.dataTypes.Tuple;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;

import java.util.Map;

public abstract class BasicQuestion {

    /**
     * Converts value holder for the question into question settings which can be used by the user in the quiz.
     *
     * This is implemented by the questions
     * @param questionSettings the question settings that will be converted into the settings for that question
     * @return
     * @throws IllegalArgumentException thrown on invalid settings given in holder for the question type
     */
    public static Map<String, Object> convertQuestionSettings(ValueHolder questionSettings) throws IllegalArgumentException{
        throw new UnsupportedOperationException("This method must be implemented in subclasses.");
    }

    /**
     * Gets the Question and its description from the question settings
     * @param questionSettings The settings holding the question and the description
     * @return a tuple with the first value being the question and second being the description
     * @throws IllegalArgumentException
     */
    public static Tuple<String, String> getQuestionAndDescription(Map<String, ValueHolder> questionSettings) throws IllegalArgumentException{
        if(!questionSettings.containsKey("Question") || !questionSettings.containsKey("Description")){
            throw new IllegalArgumentException("Question settings must contain a question and a description.");
        }

        String question = (String) questionSettings.get("Question").getValue();

        String description = (String) questionSettings.get("Description").getValue();

        return new Tuple<>(question, description);
    }
}
