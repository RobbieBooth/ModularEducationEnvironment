package org.robbie.modulareducationenvironment.eventHandler;

enum QuizClientSideEvent {
    SAVE_QUIZ,
    OPEN_QUIZ,
    CLOSE_QUIZ,
    SUBMIT_QUIZ,
    MOVE_QUESTION,
}


public class QuizEvent extends GenericEvent {

    private QuizClientSideEvent quizClientSideEvent;

    public QuizEvent(QuizClientSideEvent quizClientSideEvent) {
        this.quizClientSideEvent = quizClientSideEvent;
    }

    public QuizClientSideEvent getEvent() {
        return quizClientSideEvent;
    }

    @Override
    public String toString() {
        return quizClientSideEvent.toString();
    }
}
