package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupSetting extends BaseSetting {
    private List<BaseSetting> children;

    public GroupSetting(String label, String tooltip, boolean required, boolean disabled, List<BaseSetting> children) {
        super(label, tooltip, SettingType.Group, required, disabled);
        this.children = children;
    }

    public GroupSetting() {
        super(SettingType.Group);
    }

    @Override
    public ValueHolder getValueHolder() {
        Map<String, Object> labelToValue = children.stream()
                .collect(Collectors.toMap(BaseSetting::getLabel, BaseSetting::getValueHolder));

        return new ValueHolder(labelToValue, this.getType());
    }

    // Getters and setters
    public List<BaseSetting> getChildren() {
        return children;
    }
    public void setChildren(List<BaseSetting> children) {
        this.children = children;
    }
}

