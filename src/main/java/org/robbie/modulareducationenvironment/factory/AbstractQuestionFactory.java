package org.robbie.modulareducationenvironment.factory;

import org.robbie.modulareducationenvironment.QuestionState;

public abstract class AbstractQuestionFactory {

    /**
     * Subclasses will override this method in order to create specific question
     * objects.
     */
    public abstract String createPage(QuestionState state);
}
