package org.robbie.modulareducationenvironment.modules.AIMultiChoice.controller;
import org.robbie.modulareducationenvironment.modules.AIMultiChoice.model.AIQuestionModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Arrays;

@Controller
public class AIQuestionController {

//    @GetMapping("/question/")
//    public String showQuestionPage(Model model) {
//        // Example of fetching question data
//        AIQuestionModel question = new AIQuestionModel(
//                "What is the capital of France?",
//                Arrays.asList("Paris", "London", "Berlin", "Madrid"),
//                "Paris"
//        );

    @GetMapping("/static-page")
    public String redirectToStaticPage() {
        return "modules/AIMultiChoice/index.html"; // Refers to src/main/resources/static/static-page.html
    }
}
