package org.robbie.modulareducationenvironment.moduleHandler;

import org.robbie.modulareducationenvironment.ModularEducationEnvironmentApplication;
import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.QuizQuestion;
import org.robbie.modulareducationenvironment.factory.AbstractQuestionFactory;


public class ModuleLoader {

//    public static Object invokeModuleMethod(String moduleName, String className, String methodName) throws Exception {
//        // Build the fully qualified class name
//        String fullClassName = ModularEducationEnvironmentApplication.environmentPath + ".modules." + moduleName + "." + className;
//
//        // Load the class dynamically
//        Class<?> clazz = Class.forName(fullClassName);
//
//        // Instantiate the class
//        AbstractQuestionFactory instance = (AbstractQuestionFactory) clazz.getDeclaredConstructor().newInstance();
//
//        return instance.createPage(new QuestionState());
//    }

    public static Object invokeFactory(String moduleName, QuestionState questionState) throws Exception {
        String fullClassName = ModularEducationEnvironmentApplication.environmentPath + ".modules." + moduleName + ".factory.QuestionFactory";

        // Load the class dynamically
        Class<?> clazz = Class.forName(fullClassName);

        // Instantiate the class
        AbstractQuestionFactory instance = (AbstractQuestionFactory) clazz.getDeclaredConstructor().newInstance();

        return instance.createPage(questionState);
    }

    public static QuizQuestion getQuizQuestion(String moduleName) throws Exception {
        String fullClassName = ModularEducationEnvironmentApplication.environmentPath + ".modules." + moduleName + ".factory.QuestionEventHandler";

        // Load the class dynamically
        Class<?> clazz = Class.forName(fullClassName);

        // Instantiate the class
        QuizQuestion instance = (QuizQuestion) clazz.getDeclaredConstructor().newInstance();

        return instance;
    }
}

