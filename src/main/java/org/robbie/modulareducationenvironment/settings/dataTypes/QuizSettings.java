package org.robbie.modulareducationenvironment.settings.dataTypes;

import com.fasterxml.jackson.databind.cfg.BaseSettings;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleConfig;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleLoader;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.QuestionSettingReader;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.ConditionalBoolSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.GroupSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.ToggleSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.ToggleDisplayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Used to be sent back and forth by the SettingsController when creating and editing a quiz
 */
public class QuizSettings {
    private UUID quizUUID;
    private UUID currentVersionUUID;
    private BaseSetting quizSetting;
    private Map<String, BaseSetting> defaultModuleSettings;
    private Map<UUID, Pair<String, BaseSetting>> questions; // List of questions - we will break them down from the list version - uuids that are no longer there will be removed from iteration
    private List<Pair<String, BaseSetting>> newQuestions;// List of new questions that we will create

    // Constructors
    public QuizSettings(UUID quizUUID, UUID currentVersionUUID, BaseSetting quizSetting, Map<String, BaseSetting> defaultModuleSettings, Map<UUID, Pair<String, BaseSetting>> questions, List<Pair<String, BaseSetting>> newQuestions) {
        this.quizUUID = quizUUID;
        this.currentVersionUUID = currentVersionUUID;
        this.quizSetting = quizSetting;
        this.defaultModuleSettings = defaultModuleSettings;
        this.questions = questions;
        this.newQuestions = newQuestions;
    }

    public static QuizSettings createDefaultQuizSettings(ModuleConfig moduleConfig) {
        UUID quizUUID = UUID.randomUUID();
        UUID currentVersionUUID = UUID.randomUUID();
        BaseSetting quizSettings;
        try{
            ClassPathResource resource = new ClassPathResource("static/DefaultQuizSettings.json");
            quizSettings = QuestionSettingReader.readSettingJson(resource.getFile());
        } catch (IOException e) {
            //TODO just add a description settings option for this exact problem
            quizSettings = new GroupSetting(
                    "Error loading default settings",
                    e.getMessage(),
                    false,
                    false,
                    new ArrayList<>()
            );
        }

        Map<String, BaseSetting> defaultModuleSettings = moduleConfig.getDefaultModuleSettingMap();
        return new QuizSettings(
                quizUUID,
                currentVersionUUID,
                quizSettings,
                defaultModuleSettings,
                new HashMap<>(),
                new ArrayList<>()
        );

    }

    public UUID getQuizUUID() {
        return quizUUID;
    }

    public UUID getCurrentVersionUUID() {
        return currentVersionUUID;
    }

    public BaseSetting getQuizSetting() {
        return quizSetting;
    }

    public Map<String, BaseSetting> getDefaultModuleSettings() {
        return defaultModuleSettings;
    }

    public Map<UUID, Pair<String, BaseSetting>> getQuestions() {
        return questions;
    }

    public List<Pair<String, BaseSetting>> getNewQuestions() {
        return newQuestions;
    }
}

