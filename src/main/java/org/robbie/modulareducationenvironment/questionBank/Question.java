package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class Question {

    private String moduleName;
    private UUID questionTemplateUUID;

    public Question(String moduleName, UUID questionTemplateUUID) {
        this.moduleName = moduleName;
        this.questionTemplateUUID = questionTemplateUUID;
    }

    // Constructors
    public Question(String moduleName) {
        this.moduleName = moduleName;
    }

    public studentQuestionAttempt createStudentQuestionAttempt() {
        UUID studentQuestionID = UUID.randomUUID();
        return new studentQuestionAttempt(studentQuestionID, this.questionTemplateUUID, this.moduleName);
    }

    public Question() {
    }

    public String getModuleName() {
        return moduleName;
    }

    public UUID getQuestionTemplateUUID() {
        return questionTemplateUUID;
    }
}
