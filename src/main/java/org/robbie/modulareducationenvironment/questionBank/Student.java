package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document("Student")
public class Student {

    @Id
    private UUID studentUUID;
    private List<UUID> availableQuiz;
    private List<UUID> attemptedQuiz;

    public Student(UUID studentUUID, List<UUID> availableQuiz, List<UUID> attemptedQuiz) {
        this.studentUUID = studentUUID;
        this.availableQuiz = availableQuiz;
        this.attemptedQuiz = attemptedQuiz;
    }

    public Student(UUID studentUUID) {
        this.studentUUID = studentUUID;
        this.availableQuiz = new ArrayList<>();
        this.attemptedQuiz = new ArrayList<>();
    }

    public Student() {
    }

    public UUID getStudentUUID() {
        return studentUUID;
    }

    public List<UUID> getAvailableQuiz() {
        return availableQuiz;
    }

    public List<UUID> getAttemptedQuiz() {
        return attemptedQuiz;
    }

    public void addAvailableQuiz(UUID quizID) {
        availableQuiz.add(quizID);
    }

    public void addAttemptedQuiz(UUID quizID) {
        attemptedQuiz.add(quizID);
    }
}
