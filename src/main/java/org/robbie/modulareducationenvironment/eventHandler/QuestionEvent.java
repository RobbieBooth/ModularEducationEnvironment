package org.robbie.modulareducationenvironment.eventHandler;

enum QuestionClientSideEvent{
    SAVE_QUESTION,
    SUBMIT_QUESTION,
    TOGGLE_FLAG
}

public class QuestionEvent extends GenericEvent{
    private QuestionClientSideEvent event;

    public QuestionEvent(QuestionClientSideEvent event) {
        this.event = event;
    }

    public QuestionClientSideEvent getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return event.toString();
    }
}
