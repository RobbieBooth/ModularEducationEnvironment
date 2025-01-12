package org.robbie.modulareducationenvironment.eventHandler;

enum QuizClientSideEvent {
    SAVE_QUIZ,
    OPEN_QUIZ,
    START_QUIZ,
    CLOSE_QUIZ,
    SUBMIT_QUIZ,
    MOVE_QUESTION,
}


public class QuizEvent extends GenericEvent {

    private QuizClientSideEvent event;

    public QuizEvent(QuizClientSideEvent event) {
        this.event = event;
    }

    public QuizClientSideEvent getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return event.toString();
    }
}
