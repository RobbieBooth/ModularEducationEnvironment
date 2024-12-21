package org.robbie.modulareducationenvironment.modules.AIMultiChoice.factory;

import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.factory.AbstractQuestionFactory;

public class QuestionFactory extends AbstractQuestionFactory {
    @Override
    public String createPage(QuestionState state) {
        return "index.html";
    }
}
