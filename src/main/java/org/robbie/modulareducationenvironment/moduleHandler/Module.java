package org.robbie.modulareducationenvironment.moduleHandler;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Module {
    private String name;
    private Map<String, String> globalSettings;
    private BaseSetting defaultQuestionSettings;

    public Module() {
        name = null;
        globalSettings = new HashMap<>();
        defaultQuestionSettings = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getGlobalSettings() {
        return globalSettings;
    }

    public void setGlobalSettings(Map<String, String> globalSettings) {
        this.globalSettings = globalSettings;
    }

    public BaseSetting getDefaultQuestionSettings() {
        return defaultQuestionSettings;
    }

    public void setDefaultQuestionSettings(BaseSetting defaultQuestionSettings) {
        this.defaultQuestionSettings = defaultQuestionSettings;
    }

    public static Module findModuleByName(List<Module> modules, String moduleName) {
        return modules.stream()
                .filter(module -> module.getName().equals(moduleName))
                .findFirst()
                .orElse(null); // Returns null if not found
    }
}
