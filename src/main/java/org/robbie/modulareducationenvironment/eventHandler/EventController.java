package org.robbie.modulareducationenvironment.eventHandler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.robbie.modulareducationenvironment.QuizQuestion;
import org.robbie.modulareducationenvironment.jwt.JwtAuthenticationFilter;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleConfig;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleLoader;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleMessagingService;
import org.robbie.modulareducationenvironment.questionBank.*;
import org.robbie.modulareducationenvironment.userManagement.UserRepository;
import org.robbie.modulareducationenvironment.userManagement.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EventController {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    @Autowired
    private ModuleMessagingService moduleMessagingService;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModuleConfig moduleConfig;

    //JWT secret key
    @Value("${app.jwt.verifier.key}")
    private String SECRET;

    @MessageMapping("/send")
    @SendTo("/topic/event")
    public ResponseEntity<studentQuizAttempt> sendMessage(EventDetails event, SimpMessageHeaderAccessor headerAccessor) {
        // Access the token from the session attributes
        String token = (String) headerAccessor.getSessionAttributes().get("token");

        //Get user who requested id
        UUID userUUID;
        try{
            userUUID = getUUIDFromToken(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<studentQuizAttempt> optionalQuizAttempt = quizAttemptRepository.findById(event.getQuizUUID());
        if (optionalQuizAttempt.isPresent()) {
            studentQuizAttempt studentQuizAttempt = optionalQuizAttempt.get();
            List<studentQuestionAttempt> questions = studentQuizAttempt.getQuestions();

            //if flag event do that otherwise regular dispatch
            if(event.getGenericEvent() instanceof QuestionEvent) {
                QuestionEvent quizEvent = (QuestionEvent) event.getGenericEvent();
                if(quizEvent.getEvent().equals(QuestionClientSideEvent.TOGGLE_FLAG)){
                    studentQuizAttempt.toggleFlagOnQuestion(event.getQuestionUUID());
                    //Update the database
                    quizAttemptRepository.save(studentQuizAttempt);
                    //Send update to the front end for the flagged question
                    moduleMessagingService.sendQuestionUpdate(studentQuizAttempt.getStudentQuizAttemptUUID(), studentQuizAttempt.getQuestion(event.getQuestionUUID()).orElse(null));
                    return ResponseEntity.ok(null);
                }
            }

            //Create all the question objects then process events on them
            LinkedHashMap<UUID, QuizQuestion> quizQuestionMap = questions.stream().collect(Collectors.toMap(
                    question -> question.getStudentQuestionAttemptUUID(), // Key: UUID of the question
                    question -> {
                        try {
                            return ModuleLoader.getQuizQuestion(question.getModuleName());
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to fetch question for UUID: " + question.getStudentQuestionAttemptUUID(), e);
                        }
                    },
                    (existing, replacement) -> existing,// wont happen but is needed for next line
                    LinkedHashMap::new // Specify LinkedHashMap to preserve insertion order
            ));
            EventDirector.directEvent(event, studentQuizAttempt, quizQuestionMap, moduleConfig.getModules());


//            List<QuizQuestion> allQuizQuestions = questions.stream().map(question -> {
//                try {
//                    return ModuleLoader.getQuizQuestion(question.getModuleName());// TODO get question state based on question uuid
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }).collect(Collectors.toList());


        }
        return ResponseEntity.ok(null);
    }

//    @MessageMapping("/startQuiz")
//    @SendTo("/topic/event")
//    public ResponseEntity<studentQuizAttempt> getQuizById(EventDetails event, SimpMessageHeaderAccessor headerAccessor) {
//        // Access the token from the session attributes
//        String token = (String) headerAccessor.getSessionAttributes().get("token");
//
//        //Get user who requested id
//        UUID userUUID;
//        try{
//            userUUID = getUUIDFromToken(token);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        GenericEvent genericEvent = event.getGenericEvent();
//        if(genericEvent instanceof QuizEvent) {
//            QuizEvent quizEvent = (QuizEvent) genericEvent;
//            if(quizEvent.getEvent().equals(QuizClientSideEvent.START_QUIZ)){
//                Optional<Quiz> quiz = quizRepository.findFirstByQuizUUIDOrderByCreatedAtDesc(event.getQuizUUID());
//                Optional<User> student = userRepository.findById(userUUID);
//                if(!quiz.isPresent() || !student.isPresent()) {
//                    return ResponseEntity.notFound().build();
//                }
//                studentQuizAttempt value = quizAttemptRepository.save(quiz.get().createStudentQuizAttempt(userUUID));
////                student.get().addAttemptedQuiz(value.getStudentQuizAttemptUUID());//TODO fix here when adding attempts to class
//                return ResponseEntity.ok(value);
//            }
//            if(quizEvent.getEvent().equals(QuizClientSideEvent.OPEN_QUIZ)){
//                Optional<studentQuizAttempt> optionalQuizAttempt = quizAttemptRepository.findById(event.getQuizUUID());
//                if(!optionalQuizAttempt.isPresent()) {
//                    return ResponseEntity.notFound().build();
//                }
//                return ResponseEntity.ok(optionalQuizAttempt.get());
//            }
//        }
//        //ERROR since its not right event or values
//        return ResponseEntity.notFound().build();
//    }

    public UUID getUUIDFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);

        String userUUID = jwt.getClaim("uuid").asString();
        return UUID.fromString(userUUID);
    }
}

