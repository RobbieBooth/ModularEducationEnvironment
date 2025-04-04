package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListSetting extends BaseSetting {
    private List<BaseSetting> children = new ArrayList<>();
    private boolean allowAddition;
    private boolean allowRemoval;
    private Integer maxAmount;
    private Integer minAmount;
    private BaseSetting settingToAdd;
    private Boolean haveBorder = true; //Used for front end on whether to have padding and border - this is more for style

    public ListSetting(String label, String tooltip, boolean required, boolean disabled, List<BaseSetting> children, boolean allowAddition, boolean allowRemoval, Integer maxAmount, Integer minAmount, BaseSetting settingToAdd, Boolean haveBorder) {
        super(label, tooltip, SettingType.ListSetting, required, disabled);
        this.children = children;
        this.allowAddition = allowAddition;
        this.allowRemoval = allowRemoval;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.settingToAdd = settingToAdd;
        this.haveBorder = haveBorder;
    }

    public ListSetting() {
        super(SettingType.ListSetting);
    }

    @Override
    public ValueHolder getValueHolder() {
        if(this.children == null){
            this.children = new ArrayList<>();
        }
        List<ValueHolder> allChildren = children.stream().map((setting) -> setting.getValueHolder()).collect(Collectors.toList());
        return new ValueHolder(allChildren, this.getType());
    }

    // Getters and setters
    public List<BaseSetting> getChildren() {
        return children;
    }
    public void setChildren(List<BaseSetting> children) {
        this.children = children;
    }

    public boolean isAllowAddition() {
        return allowAddition;
    }
    public void setAllowAddition(boolean allowAddition) {
        this.allowAddition = allowAddition;
    }

    public boolean isAllowRemoval() {
        return allowRemoval;
    }
    public void setAllowRemoval(boolean allowRemoval) {
        this.allowRemoval = allowRemoval;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }
    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getMinAmount() {
        return minAmount;
    }
    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public BaseSetting getSettingToAdd() {
        return settingToAdd;
    }
    public void setSettingToAdd(BaseSetting settingToAdd) {
        this.settingToAdd = settingToAdd;
    }

    public Boolean getHaveBorder() {
        return haveBorder;
    }
}

