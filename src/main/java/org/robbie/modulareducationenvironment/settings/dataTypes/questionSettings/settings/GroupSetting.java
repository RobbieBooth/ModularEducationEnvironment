package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.List;

public class GroupSetting extends BaseSetting {
    private List<BaseSetting> children;

    public GroupSetting(String label, String tooltip, boolean required, boolean disabled, List<BaseSetting> children) {
        super(label, tooltip, SettingType.Group, required, disabled);
        this.children = children;
    }

    public GroupSetting() {
        super(SettingType.Group);
    }

    // Getters and setters
    public List<BaseSetting> getChildren() {
        return children;
    }
    public void setChildren(List<BaseSetting> children) {
        this.children = children;
    }
}

