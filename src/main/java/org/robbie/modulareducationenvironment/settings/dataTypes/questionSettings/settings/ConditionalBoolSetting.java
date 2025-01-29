package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

public class ConditionalBoolSetting extends BaseSetting {
    private ToggleSetting condition;
    private BaseSetting children;
    private boolean not;

    public ConditionalBoolSetting(String label, String tooltip, boolean required, boolean disabled, ToggleSetting condition, BaseSetting children, boolean not) {
        super(label, tooltip, SettingType.ConditionalBool, required, disabled);
        this.condition = condition;
        this.children = children;
        this.not = not;
    }

    public ConditionalBoolSetting() {
        super(SettingType.ConditionalBool);
    }

    // Getters and setters
    public ToggleSetting getCondition() {
        return condition;
    }
    public void setCondition(ToggleSetting condition) {
        this.condition = condition;
    }

    public BaseSetting getChildren() {
        return children;
    }
    public void setChildren(BaseSetting children) {
        this.children = children;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }
}
