package org.robbie.modulareducationenvironment.questionBank;

import org.robbie.modulareducationenvironment.moduleHandler.Module;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleConfig;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleConfigProvider;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleLoader;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Question {
    private static final Logger logger = LoggerFactory.getLogger(Question.class);

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
        ModuleConfig moduleConfig = ModuleConfigProvider.getModuleConfig();
        if(moduleConfig == null) {
            logger.error("No module config found so cannot create settings");
            //TODO error
        }
        //Get the global settings
        Module theModule = moduleConfig.getModuleMap().get(moduleName);
        Map<String, String> globalSettings = new HashMap<>();
        if (theModule == null) {
            globalSettings = theModule.getGlobalSettings();
        }

        Map<String, Object> questionSettings = new HashMap<>();
        try{
            questionSettings = ModuleLoader.createQuestionSettings(moduleName, globalSettings, questionSetting);
        } catch (Exception e) {
            logger.error("Error creating modules settings: {}", e.getMessage());
        }

        Map<String, Object> additionalData = new HashMap<>();
        try{
            additionalData = ModuleLoader.createQuestionAdditionalData(moduleName, globalSettings, questionSettings);
        } catch (Exception e) {
            logger.error("Error creating modules additionalData: {}", e.getMessage());
        }

        return new studentQuestionAttempt(studentQuestionID, this.questionTemplateUUID, this.moduleName, additionalData, questionSettings);
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
