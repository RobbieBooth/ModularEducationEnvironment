package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

