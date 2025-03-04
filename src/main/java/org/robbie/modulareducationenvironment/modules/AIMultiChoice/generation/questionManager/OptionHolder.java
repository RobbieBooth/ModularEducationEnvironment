package org.robbie.modulareducationenvironment.modules.AIMultiChoice.generation.questionManager;

public class OptionHolder {
    private Boolean answer;
    private String option;
    public OptionHolder(Boolean answer, String option) {
        this.answer = answer;
        this.option = option;
    }

    public OptionHolder() {
    }

    public Boolean getAnswer() {
        return answer;
    }

    public String getOption() {
        return option;
    }
}
