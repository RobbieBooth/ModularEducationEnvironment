package org.robbie.modulareducationenvironment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;

@Controller
public class QuizHandler {
    @GetMapping("/quiz/{moduleName}")
    public String showModuleQuestion(@PathVariable("moduleName") String moduleName) {
        // Add common attributes

        // Dynamically resolve view for the given module
        return "./modules/"+ moduleName + "/view/" + moduleName; // E.g., `question1/view/question1.html`
    }

//    @GetMapping("/")
//    public String showHello() {
//        // Add common attributes
//
//        // Dynamically resolve view for the given module
//        return ""; // E.g., `question1/view/question1.html`
//    }

}
