package org.robbie.modulareducationenvironment.moduleHandler;

import org.robbie.modulareducationenvironment.ModularEducationEnvironmentApplication;
import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.QuizQuestion;
import org.robbie.modulareducationenvironment.SpringContext;
import org.robbie.modulareducationenvironment.factory.AbstractQuestionFactory;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.Map;

@Service
public class ModuleLoader {

    public static ApplicationContext getApplicationContext() {
        return SpringContext.getApplicationContext();
    }

    public static Object invokeFactory(String moduleName, QuestionState questionState) throws Exception {
        String fullClassName = ModularEducationEnvironmentApplication.environmentPath + ".modules." + moduleName + ".factory.QuestionFactory";

        // Load the class dynamically
        Class<?> clazz = Class.forName(fullClassName);

        // Instantiate the class
        AbstractQuestionFactory instance = (AbstractQuestionFactory) clazz.getDeclaredConstructor().newInstance();

        return instance.createPage(questionState);
    }

    public static BaseSetting getDefaultSetting(String moduleName, Map<String, String> globalSettings) throws Exception {
        String fullClassName = ModularEducationEnvironmentApplication.environmentPath + ".modules." + moduleName + ".factory.QuestionFactory";

        // Load the class dynamically
        Class<?> clazz = Class.forName(fullClassName);

        // Instantiate the class
        AbstractQuestionFactory instance = (AbstractQuestionFactory) clazz.getDeclaredConstructor().newInstance();

        return instance.getDefaultQuestionSettings(globalSettings);
    }

    public static Map<String, Object> createQuestionSettings(String moduleName, Map<String, String> globalSettings, BaseSetting questionSettings) throws Exception {
        String fullClassName = ModularEducationEnvironmentApplication.environmentPath + ".modules." + moduleName + ".factory.QuestionFactory";

        // Load the class dynamically
        Class<?> clazz = Class.forName(fullClassName);

        // Instantiate the class
        AbstractQuestionFactory instance = (AbstractQuestionFactory) clazz.getDeclaredConstructor().newInstance();

        return instance.createQuestionSettings(globalSettings, questionSettings);
    }

    public static String getModuleResourcePath(String moduleName) {
        return "/modules/"+moduleName+"/";
    }

    public static QuizQuestion getQuizQuestion(String moduleName) throws Exception {
        String fullClassName = ModularEducationEnvironmentApplication.environmentPath + ".modules." + moduleName + ".QuestionEventHandler";

        // Load the class dynamically
        Class<?> clazz = Class.forName(fullClassName);

        // Get the ModuleSaveService bean from Spring
        ModuleSaveService moduleSaveService = getApplicationContext().getBean(ModuleSaveService.class);
        ModuleMessagingService moduleMessagingService = getApplicationContext().getBean(ModuleMessagingService.class);

        // Instantiate using the constructor that takes ModuleSaveService
        Constructor<?> constructor = clazz.getDeclaredConstructor(ModuleSaveService.class, ModuleMessagingService.class);
        QuizQuestion instance = (QuizQuestion) constructor.newInstance(moduleSaveService, moduleMessagingService);

        return instance;
    }
}

