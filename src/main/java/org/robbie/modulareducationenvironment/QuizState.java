package org.robbie.modulareducationenvironment;

import org.robbie.modulareducationenvironment.questionBank.QuizAttempt;

import java.util.*;
import java.util.stream.Collectors;

public class QuizState {
    QuizAttempt quizDatabaseState;
    boolean inProgress = false;
    private LinkedHashMap<UUID, QuestionState> questionStateMap;
    private Map<String, Object> additionalData; // Arbitrary data storage

    public QuizState(QuizAttempt quizDatabaseState) {
        this.quizDatabaseState = quizDatabaseState;
        this.additionalData = new HashMap<String, Object>();
        this.questionStateMap = quizDatabaseState.getQuestions().stream()
                .collect(Collectors.toMap(
                        questionAttempt -> questionAttempt.getQuestionAttemptUUID(), // Key: use an identifier from questionAttempt
                        questionAttempt -> new QuestionState(quizDatabaseState, questionAttempt, additionalData), // Value: create a QuestionState//TODO might need to change additional data as its quiz not question data
                        (existing, replacement) -> existing,// wont happen but is needed for next line
                        LinkedHashMap::new // Specify LinkedHashMap to preserve insertion order
                ));
    }

    public QuizState(QuizAttempt quizDatabaseState, Map<String, Object> additionalData) {
        this.quizDatabaseState = quizDatabaseState;
        this.additionalData = additionalData;
        this.questionStateMap = quizDatabaseState.getQuestions().stream()
                .collect(Collectors.toMap(
                        questionAttempt -> questionAttempt.getQuestionAttemptUUID(), // Key: use an identifier from questionAttempt
                        questionAttempt -> new QuestionState(quizDatabaseState, questionAttempt, additionalData), // Value: create a QuestionState
                        (existing, replacement) -> existing,// wont happen but is needed for next line
                        LinkedHashMap::new // Specify LinkedHashMap to preserve insertion order
                ));
    }

    public QuizAttempt getQuizDatabaseState() {
        return quizDatabaseState;
    }

    public Map<UUID, QuestionState> getQuestionStateMap() {
        return questionStateMap;
    }

    /*
    Gets the next question due to the linkedHashMap having ordering of values
     */
    public Optional<QuestionState> getNextQuestion(QuestionState currentQuestionState) {
        boolean nextQuestion = false;
        for (QuestionState questionState : questionStateMap.values()) {
            if(nextQuestion){
                return Optional.of(questionState);
            }
            if(questionState.equals(currentQuestionState)){
                nextQuestion = true;
            }
        }
        return Optional.empty();
    }

    public boolean isInProgress() {
        return inProgress;
    }
}
