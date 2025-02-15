package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

public class ValueHolder{
    public Object value;
    public SettingType type;

    public ValueHolder(Object value, SettingType type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SettingType getType() {
        return type;
    }

    public void setType(SettingType type) {
        this.type = type;
    }
}
