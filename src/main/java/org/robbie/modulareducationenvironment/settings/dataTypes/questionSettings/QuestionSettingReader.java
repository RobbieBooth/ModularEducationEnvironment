package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.robbie.modulareducationenvironment.settings.dataTypes.QuizSettings;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;

import java.io.File;
import java.io.IOException;

//Reads in json files and translates them into settings objects
public class QuestionSettingReader {

    public static BaseSetting readSettingJson(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Read JSON file and map it to QuizSettings
        BaseSetting setting = objectMapper.readValue(file, BaseSetting.class);
        return setting;
    }
}
