package org.robbie.modulareducationenvironment.moduleHandler;

import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.ErrorSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.GroupSetting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.ArrayList;

@Service
public class ConfigLoader {

//    private ModuleConfig moduleConfig;

    @Bean
    public ModuleConfig moduleConfig() {
        return loadConfig("modules.yaml");
    }


    public static ModuleConfig loadConfig(String fileName) {
        Yaml yaml = new Yaml(new Constructor(ModuleConfig.class, new LoaderOptions()));

        // Load the YAML file from the classpath
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new RuntimeException("YAML configuration file not found: " + fileName);
        }

        ModuleConfig moduleConfig = yaml.load(inputStream);
        //Set up the default question settings
        moduleConfig.getModules().forEach((module -> {
            BaseSetting defaultQuestionSetting;
            try{
                //Get default questions from the module
                defaultQuestionSetting = ModuleLoader.getDefaultSetting(module.getName(), module.getGlobalSettings());
            } catch (Exception e) {
                defaultQuestionSetting = new ErrorSetting(
                        "Error",
                        "An error happened loading "+module.getName()+" config.",
                        false,
                        false,
                        "Error loading default settings for "+module.getName(),
                        e.getMessage()
                );
            }
            module.setDefaultQuestionSettings(defaultQuestionSetting);
        }));

        return moduleConfig;
    }
}

