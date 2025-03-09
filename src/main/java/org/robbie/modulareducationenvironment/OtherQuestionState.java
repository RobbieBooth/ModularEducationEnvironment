package org.robbie.modulareducationenvironment;

import org.robbie.modulareducationenvironment.questionBank.studentQuestionAttempt;
import org.robbie.modulareducationenvironment.questionBank.studentQuizAttempt;
import org.robbie.modulareducationenvironment.moduleHandler.Module;

import java.util.Map;

/**
 * Used for the QuizQuestion events, this state holds the details of the other question that triggered it and the details of the question which received the update event from this other question.
 * The `receivingQuestionState` holds the details about the question which has received this update about the other question.
 */
public class OtherQuestionState extends QuestionState {
    private QuestionState receivingQuestionState;

    public OtherQuestionState(studentQuizAttempt quizDatabaseState, studentQuestionAttempt questionDatabaseState, Map<String, Object> additionalData, Module module, QuestionState receivingQuestionState) {
        super(quizDatabaseState, questionDatabaseState, additionalData, module);
        this.receivingQuestionState = receivingQuestionState;
    }

    /**
     *
     * @param sendingQuestionState The question sending the update request
     * @param receivingQuestionState The question which has received the event from this other question
     */
    public OtherQuestionState(QuestionState sendingQuestionState, QuestionState receivingQuestionState){
        super(sendingQuestionState.getQuizDatabaseState(), sendingQuestionState.getQuestionDatabaseState(), sendingQuestionState.getAdditionalData(), sendingQuestionState.getModule());
        this.receivingQuestionState = receivingQuestionState;
    }

    public QuestionState getReceivingQuestionState() {
        return receivingQuestionState;
    }

    public void setReceivingQuestionState(QuestionState receivingQuestionState) {
        this.receivingQuestionState = receivingQuestionState;
    }
}
