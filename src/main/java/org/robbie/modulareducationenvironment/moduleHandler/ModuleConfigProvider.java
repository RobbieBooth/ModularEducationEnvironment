package org.robbie.modulareducationenvironment.moduleHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleConfigProvider {

    private static ModuleConfig moduleConfig;

    @Autowired
    public void setModuleConfig(ModuleConfig moduleConfig) {
        ModuleConfigProvider.moduleConfig = moduleConfig;
    }

    public static ModuleConfig getModuleConfig() {
        return moduleConfig;
    }
}