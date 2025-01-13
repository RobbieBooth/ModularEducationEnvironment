package org.robbie.modulareducationenvironment.eventHandler;

import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.QuizQuestion;
import org.robbie.modulareducationenvironment.QuizState;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleLoader;
import org.robbie.modulareducationenvironment.questionBank.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EventController {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private StudentRepository studentRepository;

    @MessageMapping("/send")
    @SendTo("/topic/event")
    public EventDetails sendMessage(EventDetails event) {
        System.out.println("Received event: " + event.getGenericEvent().toString());

        Optional<studentQuizAttempt> optionalQuizAttempt = quizAttemptRepository.findById(event.getQuizUUID());
        if (optionalQuizAttempt.isPresent()) {
            studentQuizAttempt studentQuizAttempt = optionalQuizAttempt.get();
            List<studentQuestionAttempt> questions = studentQuizAttempt.getQuestions();
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
            EventDirector.directEvent(event, studentQuizAttempt, quizQuestionMap);


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

    @MessageMapping("/startQuiz")
    @SendTo("/topic/event")
    public ResponseEntity<studentQuizAttempt> getQuizById(EventDetails event) {
        GenericEvent genericEvent = event.getGenericEvent();
        if(genericEvent instanceof QuizEvent) {
            QuizEvent quizEvent = (QuizEvent) genericEvent;
            if(quizEvent.getEvent().equals(QuizClientSideEvent.START_QUIZ)){
                Optional<Quiz> quiz = quizRepository.findFirstByQuizUUIDOrderByCreatedAtDesc(event.getQuizUUID());
                Optional<Student> student = studentRepository.findById(event.getStudentUUID());
                if(!quiz.isPresent() || !student.isPresent()) {
                    return ResponseEntity.notFound().build();
                }
                studentQuizAttempt value = quizAttemptRepository.save(quiz.get().createStudentQuizAttempt(event.getStudentUUID()));
                student.get().addAttemptedQuiz(value.getStudentQuizAttemptUUID());
                return ResponseEntity.ok(value);
            }
            if(quizEvent.getEvent().equals(QuizClientSideEvent.OPEN_QUIZ)){
                Optional<studentQuizAttempt> optionalQuizAttempt = quizAttemptRepository.findById(event.getQuizUUID());
                if(!optionalQuizAttempt.isPresent()) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(optionalQuizAttempt.get());
            }
        }
        //ERROR since its not right event or values
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/createData")
    public ResponseEntity<Quiz> invokeModule() {
        List<Question> questions = new ArrayList<>();
        UUID questionUUID = UUID.randomUUID();
        Question question = new Question("AIMultiChoice", questionUUID);
        questions.add(question);

        UUID quizUUID = UUID.randomUUID();
        UUID quizVersionUUID = UUID.randomUUID();
        Quiz quiz = quizRepository.save(new Quiz(quizVersionUUID, quizUUID,questions));

        return ResponseEntity.ok(quiz);
    }

    @PutMapping("/createStudent")
    public ResponseEntity<Student> createStudent() {
        UUID studentUUID = UUID.randomUUID();
        Student student = studentRepository.save(new Student(studentUUID));
        return ResponseEntity.ok(student);
    }
}

