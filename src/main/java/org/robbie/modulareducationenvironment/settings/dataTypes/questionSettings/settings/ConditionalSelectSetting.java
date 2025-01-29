package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.Map;

public class ConditionalSelectSetting extends BaseSetting {
    private SelectSetting condition;
    private Map<String, BaseSetting> groups;

    public ConditionalSelectSetting(String label, String tooltip, boolean required, boolean disabled, SelectSetting condition, Map<String, BaseSetting> groups) {
        super(label, tooltip, SettingType.ConditionalSelect, required, disabled);
        this.condition = condition;
        this.groups = groups;
    }

    public ConditionalSelectSetting() {
        super(SettingType.ConditionalSelect);
    }

    // Getters and setters
    public SelectSetting getCondition() {
        return condition;
    }
    public void setCondition(SelectSetting condition) {
        this.condition = condition;
    }

    public Map<String, BaseSetting> getGroups() {
        return groups;
    }
    public void setGroups(Map<String, BaseSetting> groups) {
        this.groups = groups;
    }
}

