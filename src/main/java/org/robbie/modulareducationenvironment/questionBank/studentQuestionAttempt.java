package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;

import java.util.Map;
import java.util.UUID;

public class studentQuestionAttempt extends Question{
    @Id
    private UUID studentQuestionAttemptUUID;
    private boolean flagged = false;
    private Map<String, Object> settings;
    private Map<String, Object> additionalData;
//    private UUID questionTemplateUUID;
//    private String moduleName;

    // Constructors
    public studentQuestionAttempt(UUID studentQuestionAttemptUUID, UUID questionTemplateUUID, String moduleName, Map<String, Object> additionalData, Map<String, Object> settings) {
        super(moduleName, questionTemplateUUID);
        this.studentQuestionAttemptUUID = studentQuestionAttemptUUID;
        this.additionalData = additionalData;
        this.settings = settings;
    }

    public UUID getStudentQuestionAttemptUUID() {
        return studentQuestionAttemptUUID;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void toggleFlagged() {
        this.flagged = !flagged;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }
}
