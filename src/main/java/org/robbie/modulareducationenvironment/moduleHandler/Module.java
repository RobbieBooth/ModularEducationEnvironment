package org.robbie.modulareducationenvironment.moduleHandler;

import java.util.HashMap;
import java.util.Map;

public class Module {
    private String name;
    private Map<String, String> settings;

    public Module() {
        name = null;
        settings = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }
}
