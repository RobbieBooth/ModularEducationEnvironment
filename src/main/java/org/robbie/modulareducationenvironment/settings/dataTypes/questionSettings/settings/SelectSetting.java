package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.ArrayList;
import java.util.List;

public class SelectSetting extends BaseSetting {
    private List<String> value = new ArrayList<>();  // Use List instead of array for flexible storage in MongoDB
    private List<String> availableValues;
    private boolean multiSelect;

    public SelectSetting(String label, String tooltip, boolean required, boolean disabled, List<String> value, List<String> availableValues, boolean multiSelect) {
        super(label, tooltip, SettingType.Select, required, disabled);
        this.value = value;
        this.availableValues = availableValues;
        this.multiSelect = multiSelect;
    }

    public SelectSetting() {
        super(SettingType.Select);
    }

    @Override
    public ValueHolder getValueHolder() {
        if(value == null){
            value = new ArrayList<>();
        }
        return new ValueHolder(value, this.getType());
    }

    public List<String> getValue() {
        return value;
    }

    // Getters and setters

    public List<String> getAvailableValues() {
        return availableValues;
    }
    public void setAvailableValues(List<String> availableValues) {
        this.availableValues = availableValues;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }
    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }
}

