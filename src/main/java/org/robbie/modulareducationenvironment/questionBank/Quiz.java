package org.robbie.modulareducationenvironment.questionBank;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
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
    private UUID classUUID;
    private Date createdAt; // Field for storing timestamp
    private List<Question> questions;
    private BaseSetting quizSettings;

    // Constructors


    public Quiz(UUID quizVersionIdentifier, UUID quizUUID, UUID classUUID, List<Question> questions, BaseSetting quizSettings) {
        this.questions = questions;
        this.quizSettings = quizSettings;
        this.classUUID = classUUID;
        this.createdAt = new Date();
        this.quizVersionIdentifier = quizVersionIdentifier;
        this.quizUUID = quizUUID;
    }


    public Quiz() {
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

    public BaseSetting getQuizSettings() {
        return quizSettings;
    }

    public void setQuizSettings(BaseSetting quizSettings) {
        this.quizSettings = quizSettings;
    }
}
