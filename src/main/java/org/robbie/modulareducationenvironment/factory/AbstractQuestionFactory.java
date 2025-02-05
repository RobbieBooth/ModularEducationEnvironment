package org.robbie.modulareducationenvironment.factory;


import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;

import java.util.Map;

public abstract class AbstractQuestionFactory {

    /**
     * Subclasses will override this method in order to create specific question
     * objects.
     */
    public abstract String createPage(QuestionState state);

    /**
     * Subclasses will override this method in order to create specific default question settings.
     */
    public abstract BaseSetting getDefaultQuestionSettings(Map<String, String> globalSettings);
}
