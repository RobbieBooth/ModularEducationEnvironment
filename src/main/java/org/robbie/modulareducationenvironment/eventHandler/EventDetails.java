package org.robbie.modulareducationenvironment.eventHandler;

import java.util.Map;
import java.util.UUID;

public class EventDetails {
    private GenericEvent genericEvent;
    private UUID quizUUID;
    private UUID questionUUID;//QuestionUUID or Null
    private UUID nextQuestionUUID;//Used when moving questions - default should be null
    private Map<String, Object> additionalData; // Arbitrary data storage

    public EventDetails() {
    }

    public GenericEvent getGenericEvent() {
        return genericEvent;
    }

    public UUID getQuizUUID() {
        return quizUUID;
    }

    public UUID getQuestionUUID() {
        return questionUUID;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public UUID getNextQuestionUUID() {
        return nextQuestionUUID;
    }
}
