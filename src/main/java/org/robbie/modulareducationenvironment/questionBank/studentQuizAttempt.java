package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Document("studentQuizAttempt")
public class studentQuizAttempt{

    @Id
    private UUID studentQuizAttemptUUID;
    private UUID quizTemplateUUID;
    private UUID quizVersionRef;
    private List<studentQuestionAttempt> questions;

    public studentQuizAttempt(UUID studentQuizAttemptUUID, UUID quizTemplateUUID, UUID quizVersionRef, List<studentQuestionAttempt> questions) {
        this.studentQuizAttemptUUID = studentQuizAttemptUUID;
        this.quizTemplateUUID = quizTemplateUUID;
        this.quizVersionRef = quizVersionRef;
        this.questions = questions;
    }

    public UUID getStudentQuizAttemptUUID() {
        return studentQuizAttemptUUID;
    }

    public UUID getQuizTemplateUUID() {
        return quizTemplateUUID;
    }

    public UUID getQuizVersionRef() {
        return quizVersionRef;
    }

    public List<studentQuestionAttempt> getQuestions() {
        return questions;
    }

    public Optional<studentQuestionAttempt> getQuestion(UUID questionUUID) {
        return questions.stream()
                .filter(question -> question.getStudentQuestionAttemptUUID().equals(questionUUID)) // Filter based on UUID
                .findFirst(); // Get the first matching question (if any)
    }
}