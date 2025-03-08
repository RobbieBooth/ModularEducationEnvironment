package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.ValueHolder;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.FileAcceptType;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types.SettingType;

import java.util.List;
import java.util.Map;

public class FileInputSetting extends BaseSetting {
    private List<String> files; // Represent files as a list of paths or filenames
    private Map<String, List<String>> fileTypesAllowed;
    private int maxFileCount;
    private long maxCumulativeFileSizeBytes;

    public FileInputSetting(String label, String tooltip, boolean required, boolean disabled, List<String> files, Map<String, List<String>> fileTypesAllowed, int maxFileCount, long maxCumulativeFileSizeBytes) {
        super(label, tooltip, SettingType.File, required, disabled);
        this.files = files;
        this.fileTypesAllowed = fileTypesAllowed;
        this.maxFileCount = maxFileCount;
        this.maxCumulativeFileSizeBytes = maxCumulativeFileSizeBytes;
    }

    public FileInputSetting() {
        super(SettingType.File);
    }

    @Override
    public ValueHolder getValueHolder() {
        return new ValueHolder(files, this.getType());
    }

    // Getters and setters
    public List<String> getFiles() {
        return files;
    }
    public void setFiles(List<String> files) {
        this.files = files;
    }

    public Map<String, List<String>> getFileTypesAllowed() {
        return fileTypesAllowed;
    }
    public void setFileTypesAllowed(Map<String, List<String>> fileTypesAllowed) {
        this.fileTypesAllowed = fileTypesAllowed;
    }

    public int getMaxFileCount() {
        return maxFileCount;
    }
    public void setMaxFileCount(int maxFileCount) {
        this.maxFileCount = maxFileCount;
    }

    public long getMaxCumulativeFileSizeBytes() {
        return maxCumulativeFileSizeBytes;
    }
    public void setMaxCumulativeFileSizeBytes(long maxCumulativeFileSizeBytes) {
        this.maxCumulativeFileSizeBytes = maxCumulativeFileSizeBytes;
    }
}

