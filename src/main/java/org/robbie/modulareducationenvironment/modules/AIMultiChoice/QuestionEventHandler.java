package org.robbie.modulareducationenvironment.modules.AIMultiChoice;

import org.robbie.modulareducationenvironment.OtherQuestionState;
import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.QuizQuestion;
import org.robbie.modulareducationenvironment.QuizState;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleMessagingService;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleSaveService;
import org.robbie.modulareducationenvironment.modules.AIMultiChoice.factory.QuestionFactory;
import org.robbie.modulareducationenvironment.modules.AIMultiChoice.generation.GenerateQuestion;
import org.robbie.modulareducationenvironment.modules.AIMultiChoice.generation.questionManager.MultipleChoiceQuestion;
import org.robbie.modulareducationenvironment.questionBank.studentQuizAttempt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component("aiMultiChoiceEventHandler")
public class QuestionEventHandler implements QuizQuestion {

    private static final Logger logger = LoggerFactory.getLogger(QuestionEventHandler.class);
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

        studentQuizAttempt quizUpdate = moduleSaveService.saveQuestionAdditionalData(quizID, questionID, studentID, additionalData);

        //send message
        moduleMessagingService.sendQuestionUpdate(quizID, quizUpdate.getQuestion(questionID).orElse(null));
    }

    @Override
    public void onThisQuestionResume(QuestionState questionState) {

    }

    @Override
    public void onThisQuestionClose(QuestionState questionState) {

    }

    @Override
    public void onThisQuestionSubmit(QuestionState questionState) {
        Map<String, Object> additionalData = questionState.getAdditionalData();

        UUID questionID = questionState.getQuestionDatabaseState().getStudentQuestionAttemptUUID();
        Boolean hasBeenGenerated = (Boolean) additionalData.get("isGenerated");
        if(!hasBeenGenerated){
            //The questions have not been generated yet so submitting does nothing
            logger.error("Tried to submit AI questions when they haven't been generated yet for question id: {}", questionID);
            return;
        }

        UUID quizID = questionState.getQuizDatabaseState().getStudentQuizAttemptUUID();

        UUID studentID = questionState.getQuizDatabaseState().getUserUUID();
        additionalData.put("isSubmitted", true);

        studentQuizAttempt quizUpdate = moduleSaveService.saveQuestionAdditionalData(quizID, questionID, studentID, additionalData);

        //send message
        moduleMessagingService.sendQuestionUpdate(quizID, quizUpdate.getQuestion(questionID).orElse(null));
    }

    @Override
    public void onThisQuestionStart(QuestionState questionState) {

    }

    @Override
    public void onQuestionStart(OtherQuestionState questionState) {

    }

    @Override
    public void onQuestionResume(OtherQuestionState questionState) {

    }

    @Override
    public void onQuestionClose(OtherQuestionState questionState) {

    }

    @Override
    public void onQuestionSubmit(OtherQuestionState questionState) {
        UUID watchingQuestionID = questionState.getQuestionDatabaseState().getQuestionTemplateUUID();
        QuestionState recievingQuestionState = questionState.getReceivingQuestionState();
        UUID receivingQuestionID = recievingQuestionState.getQuestionDatabaseState().getStudentQuestionAttemptUUID();

        Map<String, Object> settings = recievingQuestionState.getQuestionDatabaseState().getSettings();
        String linkedQuestionID = (String) settings.get("linkedQuestionID");
        if(linkedQuestionID == null){
            logger.error("Linked question id is not defined for AI Question Gen, questionID {}", receivingQuestionID);
            return;
        }
        UUID linkedQuestionUUID = UUID.fromString(linkedQuestionID);

        if(!watchingQuestionID.equals(linkedQuestionUUID)){
            //Not question we are watching so we return
            return;
        }

        Map<String, Object> additionalData = questionState.getAdditionalData();
        if(additionalData.size() == 0 || !additionalData.containsKey("studentAnswers")){
            logger.error("Linked question id has no submission, questionID {}", receivingQuestionID);
            return;
        }

        Map<String, Object> studentAnswers = (Map<String, Object>) additionalData.get("studentAnswers");
        studentAnswers.forEach((key, value) -> {
            System.out.println(value);
        });
        if(studentAnswers == null && !studentAnswers.isEmpty()){
            logger.error("Linked question id has no submission, questionID {}", receivingQuestionID);
            return;
        }
        //Get the first value from the map
        Object firstValue = studentAnswers.values().iterator().next();

        //Check if the value is a string
        if (!(firstValue instanceof String)) {
            logger.error("First submission for linked question id {}, was not a string", receivingQuestionID);
            return;
        }

        //Continue processing if it's a string
        String studentCode = (String) firstValue;

        generateAiQuestions(recievingQuestionState, studentCode);
    }

    @Override
    public void onQuestionSave(OtherQuestionState questionState) {

    }

    public void generateAiQuestions(QuestionState questionState, String studentCode) {
        Map<String, Object> additionalData = questionState.getQuestionDatabaseState().getAdditionalData();
        UUID questionID = questionState.getQuestionDatabaseState().getStudentQuestionAttemptUUID();
        Boolean hasBeenGenerated = (Boolean) additionalData.get("isGenerated");
        if(hasBeenGenerated){
            //The questions have been generated so no point regenerating
            logger.error("Tried to generate AI Questions when they have already been generated for question id: {}", questionID);
            return;
        }

        Boolean isSubmitted = (Boolean) additionalData.get("isSubmitted");
        if(isSubmitted){
            //The questions have been submitted so no point regenerating
            logger.error("Tried to generate AI Questions when the question has already been submitted for question id: {}", questionID);
            return;
        }

        Map<String, Object> settings = questionState.getQuestionDatabaseState().getSettings();
        Integer questionCount = (Integer) settings.get("numberOfQuestions");
        String codeContext = (String) settings.get("codeContext");
        String questionTopics = (String) settings.get("topicsAssessed");
        String codeTemplate = (String) settings.get("codeTemplate");
        String codeLanguage = (String) settings.get("codeLanguage");

        if(questionState.getModule() == null){
            logger.error("No module found for questionState for getting API key, for question ID: {}", questionID);
            return;
        }
        String open_api_key = (String) questionState.getModule().getGlobalSettings().get("OPEN_API_KEY");
        if(open_api_key == null){
            logger.error("No OPEN_API_KEY for for module AI Question Generation module, for question ID: {}", questionID);
            return;
        }

        List<MultipleChoiceQuestion> questions = GenerateQuestion.generateMultipleChoiceQuestions(questionCount, codeContext, questionTopics, codeTemplate, codeLanguage, studentCode, open_api_key);
        settings.put("questions", questions);

        //Update setting on database
        UUID quizID = questionState.getQuizDatabaseState().getStudentQuizAttemptUUID();
        UUID studentID = questionState.getQuizDatabaseState().getUserUUID();
        additionalData.put("questionID", questionID);
        additionalData.put("isGenerated", true);
        studentQuizAttempt quizUpdate = moduleSaveService.saveQuestionBoth(quizID, questionID, studentID, additionalData, settings);

        //send message
        moduleMessagingService.sendQuestionUpdate(quizID, quizUpdate.getQuestion(questionID).orElse(null));
    }
}
