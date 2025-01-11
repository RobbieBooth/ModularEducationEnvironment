package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizAttemptService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    public List<QuizAttempt> getQuizAttemptsByParentQuizUUID(String parentQuizUUID) {
        return quizAttemptRepository.findQuizAttemptByParentQuizUUID(parentQuizUUID);
    }
}

