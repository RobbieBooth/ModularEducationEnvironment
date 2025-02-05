package org.robbie.modulareducationenvironment.moduleHandler;


import jakarta.websocket.server.PathParam;
import org.robbie.modulareducationenvironment.QuestionState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ModuleController {

//    private final ModuleConfig config;
//
//    public ModuleController() {
//        // Load the YAML configuration
//        this.config = ConfigLoader.loadConfig("modules.yaml");
//    }

    @GetMapping("/invoke/{moduleName}")
    public String invokeModule(
            @PathVariable String moduleName) {
        try {
            // Dynamically invoke the Factory class and method with settings
            Object result = ModuleLoader.invokeFactory(moduleName, new QuestionState());
            System.out.println("modules/"+moduleName+"/"+result.toString());
            return "/modules/"+moduleName+"/"+result.toString();

//            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


}

