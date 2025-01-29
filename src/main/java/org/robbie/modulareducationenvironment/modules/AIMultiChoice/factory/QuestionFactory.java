package org.robbie.modulareducationenvironment.modules.AIMultiChoice.factory;

import org.robbie.modulareducationenvironment.QuestionState;
import org.robbie.modulareducationenvironment.factory.AbstractQuestionFactory;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleLoader;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.QuestionSettingReader;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.GroupSetting;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


public class QuestionFactory extends AbstractQuestionFactory {
    @Override
    public String createPage(QuestionState state) {
        return "index.html";
    }

    @Override
    public BaseSetting getDefaultQuestionSettings(Map<String, String> globalSettings) {
        try{
            System.out.println(ModuleLoader.getModuleResourcePath("AIMultiChoice") + "defaultQuestionSettings.json");
//            URL path = getClass().getClassLoader().getResource(ModuleLoader.getModuleResourcePath("AIMultiChoice") + "defaultQuestionSettings.json");
//            System.out.println(path.toString());
//            File file = new File(path.getFile());

            ClassPathResource resource = new ClassPathResource("static"+ModuleLoader.getModuleResourcePath("AIMultiChoice") + "defaultQuestionSettings.json");
//            Path path = resource.getFile().toPath();
//            String content = Files.readString(path);
//            System.out.println(content);

            return QuestionSettingReader.readSettingJson(resource.getFile());
        } catch (IOException e) {
            //TODO just add a description settings option for this exact problem
            return new GroupSetting(
              "Error loading default settings",
                e.getMessage(),
                    false,
                    false,
                    new ArrayList<>()
            );
        }

    }
}
