package org.robbie.modulareducationenvironment.eventHandler;

import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.QuizQuestion;
import org.robbie.modulareducationenvironment.QuizState;
import org.robbie.modulareducationenvironment.questionBank.studentQuestionAttempt;
import org.robbie.modulareducationenvironment.questionBank.studentQuizAttempt;
import org.springframework.lang.NonNull;

import java.util.*;

public class EventDirector {

    //Get quiz with all of the questions
    //create question objects of them all
    //direct events to the associated ones and finish process
    //They can then send an update event if needed
    //At this point we would delete them from the stack/memory
    public static void directEvent(EventDetails eventDetails, studentQuizAttempt quizDatabaseAttempt, LinkedHashMap<UUID, QuizQuestion> questionList) {
        GenericEvent genericEvent = eventDetails.getGenericEvent();
        if(genericEvent instanceof QuizEvent ) {
            QuizEvent event = (QuizEvent) genericEvent;
            QuizState quizState = new QuizState(quizDatabaseAttempt, eventDetails.getAdditionalData());
            callQuizEvent(eventDetails, event, quizState, questionList);
        } else if (genericEvent instanceof QuestionEvent) {
            QuestionEvent event = (QuestionEvent) genericEvent;
            UUID questionUUID = eventDetails.getQuestionUUID();
            Optional<studentQuestionAttempt> questionDatabaseAttempt = quizDatabaseAttempt.getQuestion(questionUUID);
            if(!questionDatabaseAttempt.isPresent()) {
                //error since we cant find data/question
                //TODO log error
                return;
            }

            QuestionState questionState = new QuestionState(quizDatabaseAttempt, questionDatabaseAttempt.get(), eventDetails.getAdditionalData());
            callQuestionEvent(eventDetails, event, questionState, questionList);
        } else{
            //TODO i have no idea cause error
        }
    }

    private static void callQuizEvent(EventDetails eventDetails, QuizEvent event, QuizState quizState, LinkedHashMap<UUID, QuizQuestion> questionList) {
        switch (event.getEvent()){
            case OPEN_QUIZ -> {
                // TODO come back to as we need to create the record before this event is called

                questionList.values().forEach(quizQuestion ->{
                    if(quizState.isInProgress()){
                        quizQuestion.onQuizResume(quizState);
                    }else{
                        quizQuestion.onQuizStart(quizState);
                    }
                });
                if (questionList.isEmpty()) {
                    return;
                }
                QuizQuestion firstQuestion = questionList.values().iterator().next();
                QuestionState firstQuestionState = quizState.getQuestionStateMap().values().iterator().next();
                callOpenQuestionEvent(firstQuestion, firstQuestionState, questionList);
            }
            case CLOSE_QUIZ -> {
                //Get question that we are on
                //save it and call event on others
                //call close on that question
                //and all the other
                QuizQuestion currentQuestion = questionList.get(eventDetails.getQuestionUUID());
                QuestionState currentQuestionState = quizState.getQuestionStateMap().get(eventDetails.getQuestionUUID());//TODO might be null
                callSaveQuestionEvent(currentQuestion, currentQuestionState, questionList);

                callCloseQuestionEvent(currentQuestion, currentQuestionState, questionList);
                //Call Quiz close
                questionList.values().forEach(quizQuestion -> {
                    quizQuestion.onQuizClose(quizState);
                });
            }
            case SAVE_QUIZ -> {
                questionList.values().forEach(quizQuestion -> {
                    quizQuestion.onQuizSave(quizState);
                });
            }
            case SUBMIT_QUIZ -> {
                //call save on quiz
                //then call submit on quiz
                questionList.values().forEach(quizQuestion -> {
                    quizQuestion.onQuizSave(quizState);
                });
                questionList.values().forEach(quizQuestion -> {
                    quizQuestion.onQuizSubmit(quizState);
                });
            }
            case MOVE_QUESTION -> {
                //call save then close current question
                //open next question or do nothing if no questions left as that should be submit event instead
                UUID currentQuestionUUID = eventDetails.getQuestionUUID();
                QuizQuestion currentQuestion = questionList.get(currentQuestionUUID);
                QuestionState currentQuestionState = quizState.getQuestionStateMap().get(currentQuestionUUID);
                callSaveQuestionEvent(currentQuestion, currentQuestionState, questionList);
                callCloseQuestionEvent(currentQuestion, currentQuestionState, questionList);

                //Get next Question
                boolean nextQuestionBool = false;
                QuizQuestion nextQuestion = null;
                for (QuizQuestion quizQuestion : questionList.values()) {
                    if(nextQuestionBool){
                        nextQuestion = quizQuestion;
                        break;
                    }
                    if(quizQuestion.equals(currentQuestion)){
                        nextQuestionBool = true;
                    }
                }
                if(nextQuestion != null){
                    Optional<QuestionState> nextQuestionState = quizState.getNextQuestion(currentQuestionState);

                    if(!nextQuestionState.isEmpty()){
                        callOpenQuestionEvent(nextQuestion, nextQuestionState.get(), questionList);
                    }
                    //TODO might be empty throw error

                }

            }
            default -> {
                //TODO error
            }
        }
    }



    private static void callQuestionEvent(EventDetails eventDetails, QuestionEvent event, QuestionState questionState, Map<UUID, QuizQuestion> questionList) {
        switch (event.getEvent()){
            case SAVE_QUESTION -> {
                QuizQuestion theQuestion = questionList.get(eventDetails.getQuestionUUID());
                callSaveQuestionEvent(theQuestion, questionState, questionList);
            }
            case SUBMIT_QUESTION -> {
                QuizQuestion theQuestion = questionList.get(eventDetails.getQuestionUUID());
                if(theQuestion != null) {
                    theQuestion.onThisQuestionSubmit(questionState);
                }

                questionList.values().forEach(question -> {
                    question.onQuestionSubmit(questionState);
                });
            }
            default -> {
                //TODO error
            }
        }
    }

    private static void callSaveQuestionEvent(QuizQuestion targetQuestion, QuestionState questionState, Map<UUID, QuizQuestion> questionList) {
        if(targetQuestion != null) {
            targetQuestion.onThisQuestionSave(questionState);
        }

        questionList.values().forEach(question -> {
            question.onQuestionSave(questionState);
        });
    }
    private static void callCloseQuestionEvent(QuizQuestion targetQuestion, QuestionState questionState, Map<UUID, QuizQuestion> questionList) {
        if(targetQuestion != null) {
            targetQuestion.onThisQuestionClose(questionState);
        }

        questionList.values().forEach(question -> {
            question.onQuestionClose(questionState);
        });
    }

    private static void callOpenQuestionEvent(@NonNull QuizQuestion targetQuestion, QuestionState questionState, Map<UUID, QuizQuestion> questionList) {
        if(questionState.isInProgress()){
            targetQuestion.onThisQuestionStart(questionState);
            questionList.values().forEach(question -> {
                question.onQuestionStart(questionState);
            });
            return;
        }

        //we resume otherwise
        targetQuestion.onThisQuestionResume(questionState);
        questionList.values().forEach(question -> {
            question.onQuestionResume(questionState);
        });
    }
}
