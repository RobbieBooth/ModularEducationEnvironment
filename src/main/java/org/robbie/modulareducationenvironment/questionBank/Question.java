package org.robbie.modulareducationenvironment.questionBank;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Question {

    private String moduleName;
    private UUID questionTemplateUUID;
    private BaseSetting questionSetting;

    public Question(String moduleName, UUID questionTemplateUUID) {
        this.moduleName = moduleName;
        this.questionTemplateUUID = questionTemplateUUID;
    }

    public Question(String moduleName, UUID questionTemplateUUID, BaseSetting questionSetting) {
        this.moduleName = moduleName;
        this.questionTemplateUUID = questionTemplateUUID;
        this.questionSetting = questionSetting;
    }

    // Constructors
    public Question(String moduleName) {
        this.moduleName = moduleName;
    }

    public studentQuestionAttempt createStudentQuestionAttempt() {
        UUID studentQuestionID = UUID.randomUUID();
        return new studentQuestionAttempt(studentQuestionID, this.questionTemplateUUID, this.moduleName, new HashMap<String, Object>());
    }

    public Question() {
    }

    public String getModuleName() {
        return moduleName;
    }

    public UUID getQuestionTemplateUUID() {
        return questionTemplateUUID;
    }

    public BaseSetting getQuestionSetting() {
        return questionSetting;
    }

    public void setQuestionSetting(BaseSetting questionSetting) {
        this.questionSetting = questionSetting;
    }
}
