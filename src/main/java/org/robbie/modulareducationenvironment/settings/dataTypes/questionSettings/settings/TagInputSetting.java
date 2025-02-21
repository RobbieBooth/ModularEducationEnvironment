package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.List;

public class TagInputSetting extends BaseSetting {
    private List<String> value;
    private Integer maxEntries; // Null in Java can be represented as Integer (wrapper class)

    // Constructor

    public TagInputSetting(String label, String tooltip, boolean required, boolean disabled, List<String> value, Integer maxEntries) {
        super(label, tooltip, SettingType.TagInput, required, disabled);
        this.value = value;
        this.maxEntries = maxEntries;
    }

    public TagInputSetting() {
        super(SettingType.TagInput);
    }

    public List<String> getValue() {
        return value;
    }

    public Integer getMaxEntries() {
        return maxEntries;
    }

    @Override
    public ValueHolder getValueHolder() {
        return new ValueHolder(value, this.getType());
    }
}
