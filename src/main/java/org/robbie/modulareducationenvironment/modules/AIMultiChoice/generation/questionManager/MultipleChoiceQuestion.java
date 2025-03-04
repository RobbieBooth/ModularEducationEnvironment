package org.robbie.modulareducationenvironment.modules.AIMultiChoice.generation.questionManager;

import java.util.List;
import java.util.UUID;

public class MultipleChoiceQuestion {
    private String id;
    private String type = "MultipleChoice";
    private String question;
    private String description;
    private List<OptionHolder> options;

    public MultipleChoiceQuestion(String id, String question, String description, List<OptionHolder> options) {
        this.id = id;
        this.question = question;
        this.description = description;
        this.options = options;
    }

    public MultipleChoiceQuestion(List<OptionHolder> options, String description, String question) {
        this.id = UUID.randomUUID().toString();
        this.options = options;
        this.description = description;
        this.question = question;
    }

    public MultipleChoiceQuestion() {
        this.id = UUID.randomUUID().toString();
    }

    // Getters and Setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<OptionHolder> getOptions() {
        return options;
    }

    public void setOptions(List<OptionHolder> options) {
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}

