package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Document("QuizAttempt")
public class QuizAttempt {

    @Id
    private UUID quizAttemptUUID;
    private UUID parentQuizUUID;
    private List<QuestionAttempt> questions;

    // Constructors
    public QuizAttempt() {}

    public QuizAttempt(UUID quizAttemptUUID, UUID parentQuizUUID, List<QuestionAttempt> questions) {
        this.quizAttemptUUID = quizAttemptUUID;
        this.parentQuizUUID = parentQuizUUID;
        this.questions = questions;
    }

    public UUID getQuizAttemptUUID() {
        return quizAttemptUUID;
    }

    public UUID getParentQuizUUID() {
        return parentQuizUUID;
    }

    public List<QuestionAttempt> getQuestions() {
        return questions;
    }

    public Optional<QuestionAttempt> getQuestion(UUID questionUUID) {
        return questions.stream()
                .filter(question -> question.getQuestionAttemptUUID().equals(questionUUID)) // Filter based on UUID
                .findFirst(); // Get the first matching question (if any)
    }
}