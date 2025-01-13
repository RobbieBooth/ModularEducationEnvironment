package org.robbie.modulareducationenvironment.quiz;

import org.robbie.modulareducationenvironment.questionBank.*;
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
    private StudentRepository studentRepository;

    @PutMapping("/{studentUUID}/startQuiz/{quizUUID}")
    public ResponseEntity<studentQuizAttempt> getQuizById(@PathVariable String studentUUID, @PathVariable String quizUUID) {
        UUID studentUUIDValue = UUID.fromString(studentUUID);
        Optional<Quiz> quiz = quizRepository.findFirstByQuizUUIDOrderByCreatedAtDesc(UUID.fromString(quizUUID));
        Optional<Student> optionalStudent = studentRepository.findById(studentUUIDValue);
        if(!quiz.isPresent() || !optionalStudent.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        studentQuizAttempt value = quizAttemptRepository.save(quiz.get().createStudentQuizAttempt(studentUUIDValue));
        Student student = optionalStudent.get();
        student.addAttemptedQuiz(value.getStudentQuizAttemptUUID());
        studentRepository.save(student);
        return ResponseEntity.ok(value);
    }

    @GetMapping("/{studentUUID}")
    public ResponseEntity<Student> getQuizById(@PathVariable String studentUUID) {
        UUID studentUUIDValue = UUID.fromString(studentUUID);
        Optional<Student> student = studentRepository.findById(studentUUIDValue);
        if(!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.get());
    }

    @PatchMapping("/{studentUUID}/addQuiz/{quizUUID}")
    public ResponseEntity<Student> addQuizToStudent(@PathVariable String studentUUID, @PathVariable String quizUUID) {
        UUID studentUUIDValue = UUID.fromString(studentUUID);
        Optional<Quiz> quiz = quizRepository.findFirstByQuizUUIDOrderByCreatedAtDesc(UUID.fromString(quizUUID));
        Optional<Student> optionalStudent = studentRepository.findById(studentUUIDValue);
        if(!quiz.isPresent() || !optionalStudent.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Student student = optionalStudent.get();
        student.addAvailableQuiz(quiz.get().getQuizUUID());
        Student newStudent = studentRepository.save(student);
        return ResponseEntity.ok(newStudent);
    }
}