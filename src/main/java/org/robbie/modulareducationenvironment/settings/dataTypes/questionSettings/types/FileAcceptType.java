package org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.types;

import java.util.List;
import java.util.Map;

//https://developer.mozilla.org/en-US/docs/Web/HTTP/MIME_types/Common_types
//https://react-dropzone.js.org/#!/Accepting%20specific%20file%20types
// Example usage:
// const accept: AcceptType = {
//     "image/jpeg": [],
//     "image/png": [".png"],
//     "application/pdf": [".pdf", ".PDF"],
// };

public class FileAcceptType {
    private Map<String, List<String>> acceptTypes;

    // Getters and setters
    public Map<String, List<String>> getAcceptTypes() {
        return acceptTypes;
    }
    public void setAcceptTypes(Map<String, List<String>> acceptTypes) {
        this.acceptTypes = acceptTypes;
    }
}

