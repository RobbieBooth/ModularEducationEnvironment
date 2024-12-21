package org.robbie.modulareducationenvironment.moduleHandler;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class ConfigLoader {
    public static ModuleConfig loadConfig(String fileName) {
        Yaml yaml = new Yaml(new Constructor(ModuleConfig.class, new LoaderOptions()));

        // Load the YAML file from the classpath
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new RuntimeException("YAML configuration file not found: " + fileName);
        }

        return yaml.load(inputStream);
    }
}

