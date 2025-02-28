package org.robbie.modulareducationenvironment.modules.AIMultiChoice.factory;

import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.factory.AbstractQuestionFactory;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleLoader;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.QuestionSettingReader;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.ErrorSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.GroupSetting;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


public class QuestionFactory extends AbstractQuestionFactory {
    final String MODULE_NAME = "AIMultiChoice";

    @Override
    public String createPage(QuestionState state) {
        return "index.html";
    }

    @Override
    public BaseSetting getDefaultQuestionSettings(Map<String, String> globalSettings) {
        try{
            System.out.println(ModuleLoader.getModuleResourcePath(MODULE_NAME) + "defaultQuestionSettings.json");
//            URL path = getClass().getClassLoader().getResource(ModuleLoader.getModuleResourcePath("AIMultiChoice") + "defaultQuestionSettings.json");
//            System.out.println(path.toString());
//            File file = new File(path.getFile());

            ClassPathResource resource = new ClassPathResource("static"+ModuleLoader.getModuleResourcePath("AIMultiChoice") + "defaultQuestionSettings.json");
//            Path path = resource.getFile().toPath();
//            String content = Files.readString(path);
//            System.out.println(content);

            return QuestionSettingReader.readSettingJson(resource.getFile());
        } catch (IOException e) {
            return new ErrorSetting(
              "Error",
                    "An error happened getting the quiz settings for "+MODULE_NAME,
                    false,
                    false,
                    "Error loading default settings for "+MODULE_NAME,
                    e.getMessage()
            );
        }

    }

    @Override
    public Map<String, Object> createQuestionSettings(Map<String, String> globalSettings, BaseSetting baseSetting) {
        return Map.of();
    }

    @Override
    public Map<String, Object> createAdditionalData(Map<String, String> globalSettings, Map<String, Object> questionSettings) {
        return Map.of();
    }
}
