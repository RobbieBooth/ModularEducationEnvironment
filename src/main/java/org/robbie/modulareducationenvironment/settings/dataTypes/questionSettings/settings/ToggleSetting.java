package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.ToggleDisplayType;

public class ToggleSetting extends BaseSetting {
    private boolean value;
    private ToggleDisplayType display;

    public ToggleSetting(String label, String tooltip, boolean required, boolean disabled, boolean value, ToggleDisplayType display) {
        super(label, tooltip, SettingType.Toggle, required, disabled);
        this.value = value;
        this.display = display;
    }

    public ToggleSetting() {
        super(SettingType.Toggle);
    }

    // Getters and setters
    public boolean isValue() {
        return value;
    }
    public void setValue(boolean value) {
        this.value = value;
    }

    public ToggleDisplayType getDisplay() {
        return display;
    }
    public void setDisplay(ToggleDisplayType display) {
        this.display = display;
    }
}

