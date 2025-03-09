package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

public class InputSetting extends BaseSetting {
    private String value;
    private Integer maxCharacters;
    private String maxLines;

    public InputSetting(String label, String tooltip, boolean required, boolean disabled, String value, Integer maxCharacters, String maxLines) {
        super(label, tooltip, SettingType.Input, required, disabled);
        this.value = value;
        this.maxCharacters = maxCharacters;
        this.maxLines = maxLines;
    }

    public InputSetting() {
        super(SettingType.Input);
    }

    @Override
    public ValueHolder getValueHolder() {
        return new ValueHolder(value, this.getType());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // Getters and setters

    public Integer getMaxCharacters() {
        return maxCharacters;
    }
    public void setMaxCharacters(Integer maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public String getMaxLines() {
        return maxLines;
    }
    public void setMaxLines(String maxLines) {
        this.maxLines = maxLines;
    }
}

