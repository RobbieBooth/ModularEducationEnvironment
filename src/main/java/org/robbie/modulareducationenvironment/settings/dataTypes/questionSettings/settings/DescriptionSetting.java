package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

public class DescriptionSetting extends BaseSetting {
    private String title;
    private String value;

    public DescriptionSetting(String label, String tooltip, boolean required, boolean disabled, String title, String value) {
        super(label, tooltip, SettingType.Description, required, disabled);
        this.title = title;
        this.value = value;
    }

    public DescriptionSetting() {
        super(SettingType.Description);
    }

    @Override
    public ValueHolder getValueHolder() {
        return new ValueHolder(value, this.getType());
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
}

