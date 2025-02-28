package org.robbie.modulareducationenvironment.modules.BasicQuestion;

import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.QuizQuestion;
import org.robbie.modulareducationenvironment.QuizState;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleMessagingService;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleSaveService;
import org.robbie.modulareducationenvironment.questionBank.studentQuestionAttempt;
import org.robbie.modulareducationenvironment.questionBank.studentQuizAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class QuestionEventHandler implements QuizQuestion {

    private final ModuleSaveService moduleSaveService;
    private final ModuleMessagingService moduleMessagingService;

    @Autowired
    public QuestionEventHandler(ModuleSaveService moduleSaveService, ModuleMessagingService moduleMessagingService) {
        this.moduleSaveService = moduleSaveService;
        this.moduleMessagingService = moduleMessagingService;
    }

    @Override
    public void onQuizStart(QuizState quizState) {

    }

    @Override
    public void onQuizResume(QuizState quizState) {

    }

    @Override
    public void onQuizClose(QuizState quizState) {

    }

    @Override
    public void onQuizSubmit(QuizState quizState) {

    }

    @Override
    public void onQuizSave(QuizState quizState) {

    }

    @Override
    public void onThisQuestionStart(QuestionState questionState) {

    }

    @Override
    public void onThisQuestionResume(QuestionState questionState) {

    }

    @Override
    public void onThisQuestionClose(QuestionState questionState) {

    }

    @Override
    public void onThisQuestionSubmit(QuestionState questionState) {
        UUID quizID = questionState.getQuizDatabaseState().getStudentQuizAttemptUUID();
        UUID questionID = questionState.getQuestionDatabaseState().getStudentQuestionAttemptUUID();
        UUID studentID = questionState.getQuizDatabaseState().getUserUUID();
        Map<String, Object> additionalData = questionState.getAdditionalData();
        additionalData.put("isSubmitted", true);

        studentQuizAttempt quizUpdate = moduleSaveService.saveQuestion(quizID, questionID, studentID, additionalData);
//        quizUpdate.getQuestion(questionID);
//        //Get the rest of the question data for sending to message service and add the latest additional data to it
//        studentQuestionAttempt questionUpdate = questionState.getQuestionDatabaseState();
//        questionUpdate.setAdditionalData(additionalData);

        //send message
        moduleMessagingService.sendQuestionUpdate(quizID, quizUpdate.getQuestion(questionID).orElse(null));
    }

    @Override
    public void onThisQuestionSave(QuestionState questionState) {
        Map<String, Object> additionalData = questionState.getAdditionalData();
        //Do not do save as question has been submitted already so there is nothing to save
        if(additionalData.containsKey("isSubmitted") && additionalData.get("isSubmitted") instanceof Boolean && (Boolean) additionalData.get("isSubmitted")){
            return;
        }

        UUID quizID = questionState.getQuizDatabaseState().getStudentQuizAttemptUUID();
        UUID questionID = questionState.getQuestionDatabaseState().getStudentQuestionAttemptUUID();
        UUID studentID = questionState.getQuizDatabaseState().getUserUUID();

        additionalData.put("isSubmitted", false);

        studentQuizAttempt quizUpdate = moduleSaveService.saveQuestion(quizID, questionID, studentID, additionalData);
//        quizUpdate.getQuestion(questionID);
//        //Get the rest of the question data for sending to message service and add the latest additional data to it
//        studentQuestionAttempt questionUpdate = questionState.getQuestionDatabaseState();
//        questionUpdate.setAdditionalData(additionalData);

        //send message
        moduleMessagingService.sendQuestionUpdate(quizID, quizUpdate.getQuestion(questionID).orElse(null));
    }

    @Override
    public void onQuestionStart(QuestionState questionState) {

    }

    @Override
    public void onQuestionResume(QuestionState questionState) {

    }

    @Override
    public void onQuestionClose(QuestionState questionState) {

    }

    @Override
    public void onQuestionSubmit(QuestionState questionState) {

    }

    @Override
    public void onQuestionSave(QuestionState questionState) {

    }
}
