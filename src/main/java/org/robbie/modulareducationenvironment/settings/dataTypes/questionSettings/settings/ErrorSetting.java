package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

public class ErrorSetting extends BaseSetting {
    private String title;
    private String value; // error message

    public ErrorSetting(String label, String tooltip, boolean required, boolean disabled, String title, String value) {
        super(label, tooltip, SettingType.Error, required, disabled);
        this.title = title;
        this.value = value;
    }

    public ErrorSetting() {
        super(SettingType.Error);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public ValueHolder getValueHolder() {
        return new ValueHolder(value, this.getType());
    }

    public String getValue() {
        return value;
    }
}

