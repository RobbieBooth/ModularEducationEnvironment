package org.robbie.modulareducationenvironment.quiz;

import org.robbie.modulareducationenvironment.questionBank.*;
import org.robbie.modulareducationenvironment.userManagement.UserRepository;
import org.robbie.modulareducationenvironment.userManagement.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/student")
public class QuizController {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;

//    @PutMapping("/{studentUUID}/startQuiz/{quizUUID}")
//    public ResponseEntity<studentQuizAttempt> getQuizById(@PathVariable String studentUUID, @PathVariable String quizUUID) {
//        UUID studentUUIDValue = UUID.fromString(studentUUID);
//        Optional<Quiz> quiz = quizRepository.findFirstByQuizUUIDOrderByCreatedAtDesc(UUID.fromString(quizUUID));
//        Optional<User> optionalStudent = userRepository.findById(studentUUIDValue);
//        if(!quiz.isPresent() || !optionalStudent.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        studentQuizAttempt value = quizAttemptRepository.save(quiz.get().createStudentQuizAttempt(studentUUIDValue));
//        User user = optionalStudent.get();
//        user.addAttemptedQuiz(value.getStudentQuizAttemptUUID());
//        userRepository.save(user);
//        return ResponseEntity.ok(value);
//    }

//    @GetMapping("/{studentUUID}")
//    public ResponseEntity<User> getQuizById(@PathVariable String studentUUID) {
//        UUID studentUUIDValue = UUID.fromString(studentUUID);
//        Optional<User> student = userRepository.findById(studentUUIDValue);
//        if(!student.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(student.get());
//    }
//
//    @PatchMapping("/{studentUUID}/addQuiz/{quizUUID}")
//    public ResponseEntity<User> addQuizToStudent(@PathVariable String studentUUID, @PathVariable String quizUUID) {
//        UUID studentUUIDValue = UUID.fromString(studentUUID);
//        Optional<Quiz> quiz = quizRepository.findFirstByQuizUUIDOrderByCreatedAtDesc(UUID.fromString(quizUUID));
//        Optional<User> optionalStudent = userRepository.findById(studentUUIDValue);
//        if(!quiz.isPresent() || !optionalStudent.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        User user = optionalStudent.get();
//        user.addAvailableQuiz(quiz.get().getQuizUUID());
//        User newUser = userRepository.save(user);
//        return ResponseEntity.ok(newUser);
//    }
}