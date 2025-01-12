package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Document("Quiz")
public class Quiz {

    @Id
    private UUID quizVersionIdentifier;
    private UUID quizUUID;
    private List<Question> questions;

    // Constructors
    public Quiz(UUID quizVersionIdentifier, UUID quizUUID, List<Question> questions) {
        this.quizVersionIdentifier = quizVersionIdentifier;
        this.quizUUID = quizUUID;
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public studentQuizAttempt createStudentQuizAttempt() {
        UUID studentQuizID = UUID.randomUUID();
        List<studentQuestionAttempt> studentQuestionAttempts = questions.stream().map(question -> question.createStudentQuestionAttempt()).collect(Collectors.toList());

        return new studentQuizAttempt(studentQuizID,this.quizUUID, this.quizVersionIdentifier, studentQuestionAttempts);
    }
}
