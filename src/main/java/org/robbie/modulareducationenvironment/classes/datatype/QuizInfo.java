package org.robbie.modulareducationenvironment.classes.datatype;

import org.robbie.modulareducationenvironment.questionBank.Quiz;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class QuizInfo {

    private UUID quizID;
    private String quizName;
    private String quizDescription;
    private Integer questionCount;
    private Date createdAt;
    private Optional<UUID> versionID;//latest versionID for quizzes stored on class, else it is the version id for that particular quiz in availableQuizzes of which that version has been selected


    // Constructors
    public QuizInfo() {}

    public QuizInfo(UUID quizID, String quizName, String quizDescription, Integer questionCount, Optional<UUID> versionID) {
        this.quizID = quizID;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.questionCount = questionCount;
        this.createdAt = new Date();
        this.versionID = versionID;
    }

    public QuizInfo(UUID quizID, String quizName, String quizDescription, Integer questionCount, Date createdAt, Optional<UUID> versionID) {
        this.quizID = quizID;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.questionCount = questionCount;
        this.createdAt = createdAt;
        this.versionID = versionID;
    }



    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public UUID getQuizID() {
        return quizID;
    }

    public void setQuizID(UUID quizID) {
        this.quizID = quizID;
    }

    public Optional<UUID> getVersionID() {
        return versionID;
    }

    public void setVersionID(Optional<UUID> versionID) {
        this.versionID = versionID;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Method to transform fields into a QuizInfo object
    public static QuizInfo createQuizInfo(Quiz quiz) {
        BaseSetting quizBaseSettings = quiz.getQuizSettings();
        ValueHolder quizGroupHolder = quizBaseSettings.getValueHolder();
        if(quizGroupHolder.type != SettingType.Group){
            throw new IllegalArgumentException("Quiz group setting is not of type Group");
        }
        Map<String, ValueHolder> valueHolderMap = (Map<String, ValueHolder>) quizGroupHolder.getValue();
        ValueHolder quizNameValue = valueHolderMap.get("Quiz Name");
        ValueHolder quizDescriptionValue = valueHolderMap.get("Quiz Description");

        String quizName = (String) quizNameValue.getValue();
        String quizDescription = (String) quizDescriptionValue.getValue();

        // Extract question count
        Integer questionCount = quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;

        // Create QuizInfo object
        return new QuizInfo(
                quiz.getQuizUUID(),          // Convert UUID to String for quizID
                quizName,
                quizDescription,
                questionCount,
                quiz.getCreatedAt(),
                Optional.of(quiz.getQuizVersionIdentifier()) // Convert UUID to String for versionID
        );
    }
}

