package org.robbie.modulareducationenvironment.classes.datatype;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AvailableQuiz {

    private UUID id;
    private QuizInfo quizInfo;
    private Optional<Long> startTime = Optional.empty(); // Unix time in milliseconds
    private Optional<Long> endTime = Optional.empty();   // Unix time in milliseconds
    private Optional<List<UUID>> studentsAvailableTo = Optional.empty(); // Optional
    private boolean useLatestVersion;
    private List<SampleStudentAttempt> studentAttempts;
    private boolean instantResult;
    private Optional<Integer> maxAttemptCount = Optional.empty(); // null for infinite attempts

    // Constructors
    public AvailableQuiz() {}

    public AvailableQuiz(UUID id, QuizInfo quizInfo, Optional<Long> startTime, Optional<Long> endTime, Optional<List<UUID>> studentsAvailableTo,
                         boolean useLatestVersion, List<SampleStudentAttempt> studentAttempts, boolean instantResult,
                         Optional<Integer> maxAttemptCount) {
        this.id = id;
        this.quizInfo = quizInfo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studentsAvailableTo = studentsAvailableTo;
        this.useLatestVersion = useLatestVersion;
        this.studentAttempts = studentAttempts;
        this.instantResult = instantResult;
        this.maxAttemptCount = maxAttemptCount;
    }

    // Getters and Setters


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Optional<Integer> getMaxAttemptCount() {
        return maxAttemptCount;
    }

    public void setMaxAttemptCount(Optional<Integer> maxAttemptCount) {
        this.maxAttemptCount = maxAttemptCount;
    }

    public QuizInfo getQuizInfo() {
        return quizInfo;
    }

    public void setQuizInfo(QuizInfo quizInfo) {
        this.quizInfo = quizInfo;
    }

    public Optional<Long> getStartTime() {
        return startTime;
    }

    public void setStartTime(Optional<Long> startTime) {
        this.startTime = startTime;
    }

    public Optional<Long> getEndTime() {
        return endTime;
    }

    public void setEndTime(Optional<Long> endTime) {
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

