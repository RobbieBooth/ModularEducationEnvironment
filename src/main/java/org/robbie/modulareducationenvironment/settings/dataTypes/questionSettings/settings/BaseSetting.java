package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

//Cast types
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ToggleSetting.class, name = "Toggle"),
        @JsonSubTypes.Type(value = InputSetting.class, name = "Input"),
        @JsonSubTypes.Type(value = SelectSetting.class, name = "Select"),
        @JsonSubTypes.Type(value = FileInputSetting.class, name = "File"),
        @JsonSubTypes.Type(value = ErrorSetting.class, name = "Error"),
        @JsonSubTypes.Type(value = DescriptionSetting.class, name = "Description"),
        @JsonSubTypes.Type(value = GroupSetting.class, name = "Group"),
        @JsonSubTypes.Type(value = ListSetting.class, name = "ListSetting"),
        @JsonSubTypes.Type(value = ConditionalBoolSetting.class, name = "ConditionalBool"),
        @JsonSubTypes.Type(value = ConditionalSelectSetting.class, name = "ConditionalSelect"),
})
public abstract class BaseSetting {
    private String label;
    private String tooltip;
    private SettingType type;
    private boolean required;
    private boolean disabled;

    public BaseSetting(String label, String tooltip, SettingType type, boolean required, boolean disabled) {
        this.label = label;
        this.tooltip = tooltip;
        this.type = type;
        this.required = required;
        this.disabled = disabled;
    }

    public BaseSetting() {
    }

    public BaseSetting(SettingType type) {
        this.type = type;
    }

    // Getters and setters
    public String getTooltip() {
        return tooltip;
    }
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public SettingType getType() {
        return type;
    }
    public void setType(SettingType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }
    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isDisabled() {
        return disabled;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}

