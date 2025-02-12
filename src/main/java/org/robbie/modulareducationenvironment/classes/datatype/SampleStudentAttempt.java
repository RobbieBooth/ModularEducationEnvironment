package org.robbie.modulareducationenvironment.classes.datatype;

import java.util.Optional;
import java.util.UUID;

public class SampleStudentAttempt {

    private String studentAttemptId;
    private String quizID;
    private String versionID;
    private UUID studentID;
    private Optional<Integer> grade; // Optional
    private Optional<Integer> maxGrade; // Optional

    // Constructors
    public SampleStudentAttempt() {}

    public SampleStudentAttempt(String studentAttemptId, String quizID, String versionID, UUID studentID, Optional<Integer> grade, Optional<Integer> maxGrade) {
        this.studentAttemptId = studentAttemptId;
        this.quizID = quizID;
        this.versionID = versionID;
        this.studentID = studentID;
        this.grade = grade;
        this.maxGrade = maxGrade;
    }

    // Getters and Setters
    public String getStudentAttemptId() {
        return studentAttemptId;
    }

    public void setStudentAttemptId(String studentAttemptId) {
        this.studentAttemptId = studentAttemptId;
    }

    public String getQuizID() {
        return quizID;
    }

    public void setQuizID(String quizID) {
        this.quizID = quizID;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public UUID getStudentID() {
        return studentID;
    }

    public void setStudentID(UUID studentID) {
        this.studentID = studentID;
    }

    public Optional<Integer> getGrade() {
        return grade;
    }

    public void setGrade(Optional<Integer> grade) {
        this.grade = grade;
    }

    public Optional<Integer> getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(Optional<Integer> maxGrade) {
        this.maxGrade = maxGrade;
    }
}