package org.robbie.modulareducationenvironment.eventHandler;

import org.robbie.modulareducationenvironment.QuizQuestion;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleLoader;
import org.robbie.modulareducationenvironment.questionBank.QuestionAttempt;
import org.robbie.modulareducationenvironment.questionBank.QuizAttempt;
import org.robbie.modulareducationenvironment.questionBank.QuizAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EventController {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @MessageMapping("/send")
    @SendTo("/topic/event")
    public EventDetails sendMessage(EventDetails event) {
        System.out.println("Received event: " + event.getGenericEvent().toString());
        Optional<QuizAttempt> optionalQuizAttempt = quizAttemptRepository.findById(event.getQuizUUID());
        if (optionalQuizAttempt.isPresent()) {
            QuizAttempt quizAttempt = optionalQuizAttempt.get();
            List<QuestionAttempt> questions = quizAttempt.getQuestions();
            //Create all the question objects then process events on them
            LinkedHashMap<UUID, QuizQuestion> quizQuestionMap = questions.stream().collect(Collectors.toMap(
                    question -> question.getQuestionAttemptUUID(), // Key: UUID of the question
                    question -> {
                        try {
                            return ModuleLoader.getQuizQuestion(question.getModuleName());
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to fetch question for UUID: " + question.getQuestionAttemptUUID(), e);
                        }
                    },
                    (existing, replacement) -> existing,// wont happen but is needed for next line
                    LinkedHashMap::new // Specify LinkedHashMap to preserve insertion order
            ));
            EventDirector.directEvent(event, quizAttempt, quizQuestionMap);


//            List<QuizQuestion> allQuizQuestions = questions.stream().map(question -> {
//                try {
//                    return ModuleLoader.getQuizQuestion(question.getModuleName());// TODO get question state based on question uuid
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }).collect(Collectors.toList());


        }
        return event;
    }
}

