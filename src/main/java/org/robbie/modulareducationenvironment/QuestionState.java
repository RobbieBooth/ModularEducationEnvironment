package org.robbie.modulareducationenvironment;

import org.robbie.modulareducationenvironment.questionBank.QuestionAttempt;
import org.robbie.modulareducationenvironment.questionBank.QuizAttempt;

import java.util.Map;

public class QuestionState {
    QuizAttempt quizDatabaseState;
    QuestionAttempt questionDatabaseState;
    boolean inProgress = false;
    private Map<String, Object> additionalData; // Arbitrary data storage

    public QuestionState(QuizAttempt quizDatabaseState, QuestionAttempt questionDatabaseState, Map<String, Object> additionalData) {
        this.questionDatabaseState = questionDatabaseState;
        this.quizDatabaseState = quizDatabaseState;
        this.additionalData = additionalData;
    }

    //TODO remove
    public QuestionState() {
    }

    public boolean isInProgress() {
        return inProgress;
    }
}
