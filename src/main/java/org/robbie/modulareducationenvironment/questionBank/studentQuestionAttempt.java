package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;

import java.util.Map;
import java.util.UUID;

public class studentQuestionAttempt extends Question{
    @Id
    private UUID studentQuestionAttemptUUID;
    private boolean flagged = true;
    private Map<String, Object> additionalData;
//    private UUID questionTemplateUUID;
//    private String moduleName;

    // Constructors
    public studentQuestionAttempt(UUID studentQuestionAttemptUUID, UUID questionTemplateUUID, String moduleName, Map<String, Object> additionalData) {
        super(moduleName, questionTemplateUUID);
        this.studentQuestionAttemptUUID = studentQuestionAttemptUUID;
        this.additionalData = additionalData;
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
}
