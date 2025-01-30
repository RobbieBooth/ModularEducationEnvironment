package org.robbie.modulareducationenvironment.settings.dataTypes;

import org.robbie.modulareducationenvironment.moduleHandler.ModuleConfig;
import org.robbie.modulareducationenvironment.questionBank.*;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/v1/api/setting")
public class SettingsController {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private ModuleConfig moduleConfig;

    //on no quizID specified default is given
    @GetMapping({"/{quizID}", "/"})  // Supports both /v1/api/setting/ and /v1/api/setting/{quizID}
    public ResponseEntity<QuizSettings> getQuizSettings(@PathVariable(required = false) String quizID) {
        if(quizID == null) {
            //get default
            return ResponseEntity.ok(QuizSettings.createDefaultQuizSettings(moduleConfig));
        }
        //Catch error on when the id is not assignable to a uuid
        UUID quizUUID;
        try{
            quizUUID = UUID.fromString(quizID);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        Optional<Quiz> optionalQuiz = quizRepository.findFirstByQuizUUIDOrderByCreatedAtDesc(quizUUID);
        if(!optionalQuiz.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Quiz quiz = optionalQuiz.get();
        QuizSettings quizSettings = getQuizSettings(quiz);

        return ResponseEntity.ok(quizSettings);
    }

    // Save settings for a quiz
    @PostMapping("/save")
    public ResponseEntity<QuizSettings> saveQuizSettings(@RequestBody QuizSettings settings) {
        UUID newVersionID = UUID.randomUUID();
        List<Question> questions = new ArrayList<>();
        settings.getQuestions().forEach((key, value) -> {
            questions.add(new Question(value.getFirst(), key, value.getSecond()));//module, UUID, setting
        });

        settings.getNewQuestions().forEach(value -> {
            questions.add(new Question(value.getFirst(), UUID.randomUUID(), value.getSecond()));//module, UUID, setting
        });

        Quiz newQuiz = quizRepository.save(new Quiz(newVersionID, settings.getQuizUUID(), questions, settings.getQuizSetting()));

        return ResponseEntity.ok(getQuizSettings(newQuiz));
    }


    /**
     * Get the Quiz Settings from the quiz Document provided
     * @param quiz The mongoDB quiz Document
     * @return The Quiz Settings
     */
    private QuizSettings getQuizSettings(Quiz quiz) {
        Map<UUID, Tuple<String, BaseSetting>> questionSettings = new HashMap<>();
        quiz.getQuestions().forEach((question -> {
            questionSettings.put(question.getQuestionTemplateUUID(), new Tuple(question.getModuleName(), question.getQuestionSetting()));
        }));

        QuizSettings quizSettings = new QuizSettings(
            quiz.getQuizUUID(),
                quiz.getQuizVersionIdentifier(),
                quiz.getQuizSettings(),
                moduleConfig.getDefaultModuleSettingMap(),
                questionSettings,
                new ArrayList<>()
        );
        return quizSettings;
    }
}
