package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Document("studentQuizAttempt")
public class studentQuizAttempt{

    @Id
    private UUID studentQuizAttemptUUID;

    private UUID classUUID;
    private UUID availableQuizUUID;

    private UUID quizTemplateUUID;
    private UUID quizVersionRef;
    private Date createdAt; // Field for storing timestamp
    private UUID userUUID;
    private List<studentQuestionAttempt> questions;

    public studentQuizAttempt(UUID studentQuizAttemptUUID, UUID classUUID, UUID availableQuizUUID, UUID quizTemplateUUID, UUID quizVersionRef, UUID userUUID, List<studentQuestionAttempt> questions) {
        this.studentQuizAttemptUUID = studentQuizAttemptUUID;
        this.classUUID = classUUID;
        this.availableQuizUUID = availableQuizUUID;
        this.quizTemplateUUID = quizTemplateUUID;
        this.quizVersionRef = quizVersionRef;
        this.userUUID = userUUID;
        this.questions = questions;
        this.createdAt = new Date();
    }

    public studentQuizAttempt() {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public List<studentQuestionAttempt> getQuestions() {
        return questions;
    }

    public void setQuestions(List<studentQuestionAttempt> questions) {
        this.questions = questions;
    }

    public Optional<studentQuestionAttempt> getQuestion(UUID questionUUID) {
        return questions.stream()
                .filter(question -> question.getStudentQuestionAttemptUUID().equals(questionUUID)) // Filter based on UUID
                .findFirst(); // Get the first matching question (if any)
    }

    /**
     * Toggle the flag on the question. If the question isn't found `false` is returned else `true`.
     * @param studentQuestionAttemptUUID The question to be found
     * @return if the question has been found and successfully toggled `true`, otherwise `false`
     */
    public boolean toggleFlagOnQuestion(UUID studentQuestionAttemptUUID) {
        Optional<studentQuestionAttempt> studentQuestionAttemptOptional = questions.stream()
                .filter(question -> question.getStudentQuestionAttemptUUID().equals(studentQuestionAttemptUUID)).findFirst();
        if(studentQuestionAttemptOptional.isPresent()) {
            studentQuestionAttemptOptional.get().toggleFlagged();
            return true;
        }
        return false;
    }
}