package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

public class DateSetting extends BaseSetting{

    private Long unixTimestamp;

    public DateSetting() {
        super(SettingType.Date);
    }

    public Long getUnixTimestamp() {
        return unixTimestamp;
    }

    public void setUnixTimestamp(Long unixTimestamp) {
        this.unixTimestamp = unixTimestamp;
    }

    @Override
    public ValueHolder getValueHolder() {
        return new ValueHolder(this.unixTimestamp, this.getType());
    }
}
