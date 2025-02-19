package org.robbie.modulareducationenvironment.classes.datatype;

import java.util.Optional;
import java.util.UUID;

public class SampleStudentAttempt {

    private UUID studentAttemptId;
    private UUID quizID;
    private UUID versionID;
    private UUID studentID;
    private Optional<Integer> grade = Optional.empty(); // Optional
    private Optional<Integer> maxGrade = Optional.empty(); // Optional

    // Constructors
    public SampleStudentAttempt() {}

    public SampleStudentAttempt(UUID studentAttemptId, UUID quizID, UUID versionID, UUID studentID, Optional<Integer> grade, Optional<Integer> maxGrade) {
        this.studentAttemptId = studentAttemptId;
        this.quizID = quizID;
        this.versionID = versionID;
        this.studentID = studentID;
        this.grade = grade;
        this.maxGrade = maxGrade;
    }

    public UUID getStudentAttemptId() {
        return studentAttemptId;
    }

    public void setStudentAttemptId(UUID studentAttemptId) {
        this.studentAttemptId = studentAttemptId;
    }

    public UUID getQuizID() {
        return quizID;
    }

    public void setQuizID(UUID quizID) {
        this.quizID = quizID;
    }

    public UUID getVersionID() {
        return versionID;
    }

    public void setVersionID(UUID versionID) {
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