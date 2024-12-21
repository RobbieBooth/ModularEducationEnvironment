package org.robbie.modulareducationenvironment.moduleHandler;

import org.robbie.modulareducationenvironment.QuestionState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ModuleController {

    private final ModuleConfig config;

    public ModuleController() {
        // Load the YAML configuration
        this.config = ConfigLoader.loadConfig("modules.yaml");
    }

    @GetMapping("/invoke")
    public String invokeModule(
            @RequestParam String moduleName) {
        try {
            // Dynamically invoke the Factory class and method with settings
            Object result = ModuleLoader.invokeFactory(moduleName, new QuestionState());
            return "modules/"+moduleName+"/"+result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}

