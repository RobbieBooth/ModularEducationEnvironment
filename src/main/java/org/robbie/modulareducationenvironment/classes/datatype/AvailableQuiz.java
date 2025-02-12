package org.robbie.modulareducationenvironment.classes.datatype;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AvailableQuiz {

    private String id;
    private QuizInfo quizInfo;
    private Optional<LocalDateTime> endTime; // Optional
    private Optional<List<UUID>> studentsAvailableTo; // Optional
    private boolean useLatestVersion;
    private List<SampleStudentAttempt> studentAttempts;
    private boolean instantResult;

    // Constructors
    public AvailableQuiz() {}

    public AvailableQuiz(QuizInfo quizInfo, Optional<LocalDateTime> endTime, Optional<List<UUID>> studentsAvailableTo, boolean useLatestVersion, List<SampleStudentAttempt> studentAttempts, boolean instantResult) {
        this.quizInfo = quizInfo;
        this.endTime = endTime;
        this.studentsAvailableTo = studentsAvailableTo;
        this.useLatestVersion = useLatestVersion;
        this.studentAttempts = studentAttempts;
        this.instantResult = instantResult;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QuizInfo getQuizInfo() {
        return quizInfo;
    }

    public void setQuizInfo(QuizInfo quizInfo) {
        this.quizInfo = quizInfo;
    }

    public Optional<LocalDateTime> getEndTime() {
        return endTime;
    }

    public void setEndTime(Optional<LocalDateTime> endTime) {
        this.endTime = endTime;
    }

    public Optional<List<UUID>> getStudentsAvailableTo() {
        return studentsAvailableTo;
    }

    public void setStudentsAvailableTo(Optional<List<UUID>> studentsAvailableTo) {
        this.studentsAvailableTo = studentsAvailableTo;
    }

    public boolean isUseLatestVersion() {
        return useLatestVersion;
    }

    public void setUseLatestVersion(boolean useLatestVersion) {
        this.useLatestVersion = useLatestVersion;
    }

    public List<SampleStudentAttempt> getStudentAttempts() {
        return studentAttempts;
    }

    public void setStudentAttempts(List<SampleStudentAttempt> studentAttempts) {
        this.studentAttempts = studentAttempts;
    }

    public boolean isInstantResult() {
        return instantResult;
    }

    public void setInstantResult(boolean instantResult) {
        this.instantResult = instantResult;
    }
}

