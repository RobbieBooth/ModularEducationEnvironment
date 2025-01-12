package org.robbie.modulareducationenvironment;

import org.robbie.modulareducationenvironment.questionBank.studentQuestionAttempt;
import org.robbie.modulareducationenvironment.questionBank.studentQuizAttempt;

import java.util.Map;

public class QuestionState {
    studentQuizAttempt quizDatabaseState;
    studentQuestionAttempt questionDatabaseState;
    boolean inProgress = false;
    private Map<String, Object> additionalData; // Arbitrary data storage

    public QuestionState(studentQuizAttempt quizDatabaseState, studentQuestionAttempt questionDatabaseState, Map<String, Object> additionalData) {
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
