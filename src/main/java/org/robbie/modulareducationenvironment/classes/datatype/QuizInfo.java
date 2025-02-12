package org.robbie.modulareducationenvironment.classes.datatype;

import java.util.Optional;

public class QuizInfo {

    private String quizID;
    private String quizName;
    private String quizDescription;
    private Integer questionCount;
    private String versionID;//latest versionID for quizzes stored on class, else it is the version id for that particular quiz in availableQuizzes of which that version has been selected
    private Optional<Integer> maxAttemptCount; // null for infinite attempts

    // Constructors
    public QuizInfo() {}

    public QuizInfo(String quizID, String quizName, String quizDescription, Integer questionCount, String versionID, Optional<Integer> maxAttemptCount) {
        this.quizID = quizID;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.questionCount = questionCount;
        this.versionID = versionID;
        this.maxAttemptCount = maxAttemptCount;
    }

    // Getters and Setters
    public String getQuizID() {
        return quizID;
    }

    public void setQuizID(String quizID) {
        this.quizID = quizID;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public Optional<Integer> getMaxAttemptCount() {
        return maxAttemptCount;
    }

    public void setMaxAttemptCount(Optional<Integer> maxAttemptCount) {
        this.maxAttemptCount = maxAttemptCount;
    }
}

