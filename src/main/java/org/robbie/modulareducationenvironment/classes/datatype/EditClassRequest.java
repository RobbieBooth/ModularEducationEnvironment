package org.robbie.modulareducationenvironment.classes.datatype;

import java.util.List;
import java.util.UUID;

public class EditClassRequest {
    private String className;
    private String classDescription;
    private List<UUID> educators;
    private List<UUID> students;

    // Getters and setters


    public EditClassRequest() {
    }

    public String getClassName() {
        return className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public List<UUID> getEducators() {
        return educators;
    }

    public List<UUID> getStudents() {
        return students;
    }
}

