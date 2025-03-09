package org.robbie.modulareducationenvironment.classes.datatype;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "classes")
public class Class {

    @Id
    private UUID id;
    private String className;
    private String classDescription;
    private List<UUID> educators;
    private List<UUID> students;
    private List<QuizInfo> quizzes;
    private List<AvailableQuiz> availableQuizzes;

    // Constructors
    public Class() {
        this.id = UUID.randomUUID();
    }

    public Class(String className, String classDescription, List<UUID> educators, List<UUID> students, List<QuizInfo> quizzes, List<AvailableQuiz> availableQuizzes) {
        this.id = UUID.randomUUID();
        this.className = className;
        this.classDescription = classDescription;
        this.educators = educators;
        this.students = students;
        this.quizzes = quizzes;
        this.availableQuizzes = availableQuizzes;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public List<UUID> getEducators() {
        return educators;
    }

    public void setEducators(List<UUID> educators) {
        this.educators = educators;
    }

    public List<UUID> getStudents() {
        return students;
    }

    public void setStudents(List<UUID> students) {
        this.students = students;
    }

    public List<QuizInfo> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<QuizInfo> quizzes) {
        this.quizzes = quizzes;
    }

    public List<AvailableQuiz> getAvailableQuizzes() {
        return availableQuizzes;
    }

    public void setAvailableQuizzes(List<AvailableQuiz> availableQuizzes) {
        this.availableQuizzes = availableQuizzes;
    }

    public boolean isUserInClass(UUID userId) {
        return (educators != null && educators.contains(userId)) ||
                (students != null && students.contains(userId));
    }

}
