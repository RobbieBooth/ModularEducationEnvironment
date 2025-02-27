package org.robbie.modulareducationenvironment;

import org.robbie.modulareducationenvironment.questionBank.studentQuestionAttempt;
import org.robbie.modulareducationenvironment.questionBank.studentQuizAttempt;

import java.util.Map;

public class QuestionState {
    private studentQuizAttempt quizDatabaseState;
    private studentQuestionAttempt questionDatabaseState;
    private boolean inProgress = false;
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

    public studentQuizAttempt getQuizDatabaseState() {
        return quizDatabaseState;
    }

    public void setQuizDatabaseState(studentQuizAttempt quizDatabaseState) {
        this.quizDatabaseState = quizDatabaseState;
    }

    public studentQuestionAttempt getQuestionDatabaseState() {
        return questionDatabaseState;
    }

    public void setQuestionDatabaseState(studentQuestionAttempt questionDatabaseState) {
        this.questionDatabaseState = questionDatabaseState;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }
}
