package org.robbie.modulareducationenvironment.eventHandler;

import org.robbie.modulareducationenvironment.OtherQuestionState;
import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.QuizQuestion;
import org.robbie.modulareducationenvironment.QuizState;
import org.robbie.modulareducationenvironment.moduleHandler.Module;
import org.robbie.modulareducationenvironment.questionBank.studentQuestionAttempt;
import org.robbie.modulareducationenvironment.questionBank.studentQuizAttempt;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class EventDirector {

    //Get quiz with all of the questions
    //create question objects of them all
    //direct events to the associated ones and finish process
    //They can then send an update event if needed
    //At this point we would delete them from the stack/memory
    public static void directEvent(EventDetails eventDetails, studentQuizAttempt quizDatabaseAttempt, LinkedHashMap<UUID, QuizQuestion> questionList, List<Module> modules) {
        GenericEvent genericEvent = eventDetails.getGenericEvent();
        if(genericEvent instanceof QuizEvent ) {
            QuizEvent event = (QuizEvent) genericEvent;
            QuizState quizState = new QuizState(quizDatabaseAttempt, eventDetails.getAdditionalData(), modules);
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

            studentQuestionAttempt attempt = questionDatabaseAttempt.get();
            QuestionState questionState = new QuestionState(quizDatabaseAttempt, attempt, eventDetails.getAdditionalData(), Module.findModuleByName(modules, attempt.getModuleName()));
            Map<String, Object> questionUUIDtoAdditionalDataMap = quizDatabaseAttempt.getQuestions().stream()
                    .collect(Collectors.toMap(
                            questionAttempt -> questionAttempt.getStudentQuestionAttemptUUID().toString(), // Outer key
                            questionAttempt -> questionAttempt.getAdditionalData() // Inner map from getAdditionalData
                    ));
            QuizState quizState = new QuizState(quizDatabaseAttempt, questionUUIDtoAdditionalDataMap, modules);
            callQuestionEvent(eventDetails, event, questionState, questionList, quizState);
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
                callOpenQuestionEvent(firstQuestion, firstQuestionState, questionList, quizState);
            }
            case CLOSE_QUIZ -> {
                //Get question that we are on
                //save it and call event on others
                //call close on that question
                //and all the other
                QuizQuestion currentQuestion = questionList.get(eventDetails.getQuestionUUID());
                QuestionState currentQuestionState = quizState.getQuestionStateMap().get(eventDetails.getQuestionUUID());//TODO might be null
                callSaveQuestionEvent(currentQuestion, currentQuestionState, questionList, quizState);

                callCloseQuestionEvent(currentQuestion, currentQuestionState, questionList, quizState);
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
                //Get question that we are on
                //save it and call event on others - close it then...
                QuizQuestion currentQuestion = questionList.get(eventDetails.getQuestionUUID());
                QuestionState currentQuestionState = quizState.getQuestionStateMap().get(eventDetails.getQuestionUUID());//TODO might be null
                callSaveQuestionEvent(currentQuestion, currentQuestionState, questionList, quizState);

                callCloseQuestionEvent(currentQuestion, currentQuestionState, questionList, quizState);

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
                callSaveQuestionEvent(currentQuestion, currentQuestionState, questionList, quizState);
                callCloseQuestionEvent(currentQuestion, currentQuestionState, questionList, quizState);

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
                        callOpenQuestionEvent(nextQuestion, nextQuestionState.get(), questionList, quizState);
                    }
                    //TODO might be empty throw error

                }

            }
            default -> {
                //TODO error
            }
        }
    }



    private static void callQuestionEvent(EventDetails eventDetails, QuestionEvent event, QuestionState questionState, Map<UUID, QuizQuestion> questionList, QuizState quizState) {
        switch (event.getEvent()){
            case SAVE_QUESTION -> {
                QuizQuestion theQuestion = questionList.get(eventDetails.getQuestionUUID());
                callSaveQuestionEvent(theQuestion, questionState, questionList, quizState);
            }
            case SUBMIT_QUESTION -> {
                QuizQuestion theQuestion = questionList.get(eventDetails.getQuestionUUID());
                if(theQuestion != null) {
                    theQuestion.onThisQuestionSubmit(questionState);
                }

                questionList.forEach((uuid, question) -> {
                    question.onQuestionSubmit(new OtherQuestionState(questionState, quizState.getQuestionStateMap().get(uuid)));
                });
            }
            default -> {
                //TODO error
            }
        }
    }

    private static void callSaveQuestionEvent(QuizQuestion targetQuestion, QuestionState questionState, Map<UUID, QuizQuestion> questionList, QuizState quizState) {
        if(targetQuestion != null) {
            targetQuestion.onThisQuestionSave(questionState);
        }

        questionList.forEach((uuid, question) -> {
            question.onQuestionSave(new OtherQuestionState(questionState, quizState.getQuestionStateMap().get(uuid)));
        });
    }

    private static void callCloseQuestionEvent(QuizQuestion targetQuestion, QuestionState questionState, Map<UUID, QuizQuestion> questionList, QuizState quizState) {
        if(targetQuestion != null) {
            targetQuestion.onThisQuestionClose(questionState);
        }

        questionList.forEach((uuid, question) -> {
            question.onQuestionClose(new OtherQuestionState(questionState, quizState.getQuestionStateMap().get(uuid)));
        });
    }

    private static void callOpenQuestionEvent(@NonNull QuizQuestion targetQuestion, QuestionState questionState, Map<UUID, QuizQuestion> questionList, QuizState quizState) {
        if(questionState.isInProgress()){
            targetQuestion.onThisQuestionStart(questionState);
            questionList.forEach((uuid, question) -> {
                question.onQuestionStart(new OtherQuestionState(questionState, quizState.getQuestionStateMap().get(uuid)));
            });
            return;
        }

        //we resume otherwise
        targetQuestion.onThisQuestionResume(questionState);
        questionList.forEach((uuid, question) -> {
            question.onQuestionResume(new OtherQuestionState(questionState, quizState.getQuestionStateMap().get(uuid)));
        });
    }
}
