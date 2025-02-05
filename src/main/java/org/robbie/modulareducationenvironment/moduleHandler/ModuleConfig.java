package org.robbie.modulareducationenvironment.moduleHandler;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleConfig {
    private List<Module> modules;

    public ModuleConfig() {
        modules = new ArrayList<>();
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    /**
     * Get a map of the module name to the default settings from the modules defined in the config
     * @return map of the module name to the default settings
     */
    public Map<String, BaseSetting> getDefaultModuleSettingMap() {
        Map<String, BaseSetting> map = new HashMap<>();
        for (Module module : modules) {
            map.put(module.getName(), module.getDefaultQuestionSettings());
        }
        return map;
    }

}

