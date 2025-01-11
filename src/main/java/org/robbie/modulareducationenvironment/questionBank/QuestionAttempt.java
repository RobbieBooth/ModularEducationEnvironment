package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class QuestionAttempt {
    @Id
    private UUID questionAttemptUUID;
    private String moduleName;

    // Constructors
    public QuestionAttempt() {}

    public QuestionAttempt(UUID questionAttemptUUID, String moduleName) {
        this.questionAttemptUUID = questionAttemptUUID;
        this.moduleName = moduleName;
    }

    public UUID getQuestionAttemptUUID() {
        return questionAttemptUUID;
    }

    public String getModuleName() {
        return moduleName;
    }
}
