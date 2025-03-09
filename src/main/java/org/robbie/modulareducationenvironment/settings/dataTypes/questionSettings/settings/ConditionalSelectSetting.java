package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public ValueHolder getValueHolder() {//TODO
        List<String> chosenOptions = (List<String>) condition.getValueHolder().getValue();

        // Create a map of option labels to their corresponding values
        Map<String, Object> optionToValue = chosenOptions.stream()
                .collect(Collectors.toMap(
                        option -> option, // Key: The option itself (e.g., label)
                        option -> groups.get(option).getValueHolder() // Value: The corresponding group's value
                ));

        // Return the ValueHolder with the type and the map of values
        return new ValueHolder(optionToValue, this.getType());
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

