package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class studentQuestionAttempt extends Question{
    @Id
    private UUID studentQuestionAttemptUUID;
    private boolean flagged = true;
//    private UUID questionTemplateUUID;
//    private String moduleName;

    // Constructors
    public studentQuestionAttempt(UUID studentQuestionAttemptUUID, UUID questionTemplateUUID, String moduleName) {
        super(moduleName, questionTemplateUUID);
        this.studentQuestionAttemptUUID = studentQuestionAttemptUUID;
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
}
