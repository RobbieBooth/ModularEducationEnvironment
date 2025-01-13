package org.robbie.modulareducationenvironment.eventHandler;

import java.util.Map;
import java.util.UUID;

public class EventDetails {
    private GenericEvent genericEvent;
    private UUID studentUUID;
    private UUID quizUUID;
    private UUID questionUUID;//QuestionUUID or Null
    private Map<String, Object> additionalData; // Arbitrary data storage

    public GenericEvent getGenericEvent() {
        return genericEvent;
    }

    public UUID getQuizUUID() {
        return quizUUID;
    }

    public UUID getStudentUUID() {
        return studentUUID;
    }

    public UUID getQuestionUUID() {
        return questionUUID;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }
}
