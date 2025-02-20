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
     * Updates the data for a question.
     * <p>
     * WARNING: data existing in the question will be over-ridden by `dataToSave`
     *
     * @param quizAttemptUUID     the quiz containing the question being updated
     * @param questionAttemptUUID the question to update
     * @param studentUUID         the student who the quiz belongs to. If the quiz does not belong to this student error is thrown.
     * @param dataToSave          the data to save to the question.
     * @return
     */
    public studentQuizAttempt saveQuestion(UUID quizAttemptUUID, UUID questionAttemptUUID, UUID studentUUID, Map<String, Object> dataToSave) {
        return quizAttemptRepository.findById(quizAttemptUUID).map(studentQuizAttempt -> {
            if(!studentQuizAttempt.getUserUUID().equals(studentUUID)) {
                throw new IllegalArgumentException("Auth error this is not the students attempt");
            }
            AtomicBoolean found = new AtomicBoolean(false);
            //Get the students question attempts with the new data value added
            List<studentQuestionAttempt> newStudentQuestionAttempts = studentQuizAttempt.getQuestions().stream().map(questionAttempt -> {
                if(questionAttempt.getStudentQuestionAttemptUUID().equals(questionAttemptUUID)) {
                    found.set(true);
                    questionAttempt.setAdditionalData(dataToSave);
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
}
