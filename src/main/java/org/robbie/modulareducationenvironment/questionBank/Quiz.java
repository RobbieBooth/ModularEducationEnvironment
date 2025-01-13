package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Document("Quiz")
public class Quiz {

    @Id
    private UUID quizVersionIdentifier;
    private UUID quizUUID;
    private Date createdAt; // Field for storing timestamp
    private List<Question> questions;

    // Constructors
    public Quiz(UUID quizVersionIdentifier, UUID quizUUID, List<Question> questions) {
        this.quizVersionIdentifier = quizVersionIdentifier;
        this.quizUUID = quizUUID;
        this.questions = questions;
        this.createdAt = new Date();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public UUID getQuizVersionIdentifier() {
        return quizVersionIdentifier;
    }

    public UUID getQuizUUID() {
        return quizUUID;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public studentQuizAttempt createStudentQuizAttempt(UUID studentUUID) {
        UUID studentQuizID = UUID.randomUUID();
        List<studentQuestionAttempt> studentQuestionAttempts = questions.stream().map(question -> question.createStudentQuestionAttempt()).collect(Collectors.toList());

        return new studentQuizAttempt(studentQuizID,this.quizUUID, this.quizVersionIdentifier, studentQuestionAttempts, studentUUID);
    }
}
