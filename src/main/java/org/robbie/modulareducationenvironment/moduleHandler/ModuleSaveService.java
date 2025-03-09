package org.robbie.modulareducationenvironment.moduleHandler;
import org.robbie.modulareducationenvironment.questionBank.QuizAttemptRepository;
import org.robbie.modulareducationenvironment.questionBank.studentQuestionAttempt;
import org.robbie.modulareducationenvironment.questionBank.studentQuizAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ModuleSaveService {

    private final QuizAttemptRepository quizAttemptRepository;

    @Autowired
    public ModuleSaveService(QuizAttemptRepository quizAttemptRepository) {
        this.quizAttemptRepository =  quizAttemptRepository;
    }

    /**
     * Updates the additionalData and setting for a question.
     * <p>
     * WARNING: data existing in the question will be over-ridden by `additionalData` - if its present
     * WARNING: data existing in the question will be over-ridden by `settings` - if its present
     *
     * @param quizAttemptUUID     the quiz containing the question being updated
     * @param questionAttemptUUID the question to update
     * @param studentUUID         the student who the quiz belongs to. If the quiz does not belong to this student error is thrown.
     * @param additionalData          the additional data to save to the question, if the data is empty then it is not added or overridden
     * @param settings              the setting to be saved to the question, if the setting is empty then it is not added or overridden
     * @return
     */
    private studentQuizAttempt saveQuestion(UUID quizAttemptUUID, UUID questionAttemptUUID, UUID studentUUID, Optional<Map<String, Object>> additionalData, Optional<Map<String, Object>> settings) {
        return quizAttemptRepository.findById(quizAttemptUUID).map(studentQuizAttempt -> {
            if(!studentQuizAttempt.getUserUUID().equals(studentUUID)) {
                throw new IllegalArgumentException("Auth error this is not the students attempt");
            }
            AtomicBoolean found = new AtomicBoolean(false);
            //Get the students question attempts with the new data value added
            List<studentQuestionAttempt> newStudentQuestionAttempts = studentQuizAttempt.getQuestions().stream().map(questionAttempt -> {
                if(questionAttempt.getStudentQuestionAttemptUUID().equals(questionAttemptUUID)) {
                    found.set(true);
                    if(additionalData.isPresent()) {
                        questionAttempt.setAdditionalData(additionalData.get());
                    }
                    if(settings.isPresent()) {
                        questionAttempt.setSettings(settings.get());
                    }
                }
                return questionAttempt;
            }).collect(Collectors.toList());

            //If we haven't found the item throw error.
            if(!found.get()) {
                throw new IllegalArgumentException("Question attempt with ID " + questionAttemptUUID + " does not exist on quiz attempt: "+quizAttemptUUID);
            }

            studentQuizAttempt.setQuestions(newStudentQuestionAttempts);
            return quizAttemptRepository.save(studentQuizAttempt);
        }).orElseThrow(() -> {
            // If the class does not exist, throw an exception
            throw new IllegalArgumentException("Quiz attempt with ID " + quizAttemptUUID + " does not exist.");
        });
    }

    public studentQuizAttempt saveQuestionAdditionalData(UUID quizAttemptUUID, UUID questionAttemptUUID, UUID studentUUID, Map<String, Object> additionalData) {
        return saveQuestion(quizAttemptUUID, questionAttemptUUID, studentUUID, Optional.of(additionalData), Optional.empty());
    }

    public studentQuizAttempt saveQuestionSetting(UUID quizAttemptUUID, UUID questionAttemptUUID, UUID studentUUID, Map<String, Object> setting) {
        return saveQuestion(quizAttemptUUID, questionAttemptUUID, studentUUID, Optional.empty(), Optional.of(setting));
    }
    public studentQuizAttempt saveQuestionBoth(UUID quizAttemptUUID, UUID questionAttemptUUID, UUID studentUUID, Map<String, Object> additionalData, Map<String, Object> setting) {
        return saveQuestion(quizAttemptUUID, questionAttemptUUID, studentUUID, Optional.of(additionalData), Optional.of(setting));
    }
}
