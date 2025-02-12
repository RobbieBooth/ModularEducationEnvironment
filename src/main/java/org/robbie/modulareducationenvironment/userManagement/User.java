package org.robbie.modulareducationenvironment.userManagement;

import org.robbie.modulareducationenvironment.authentication.UserRoles;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document("users")
public class User {

    @Id
    private UUID id;
    private List<ClassInfo> classes;

    public User(UUID studentUUID, List<ClassInfo> classes) {
        this.id = studentUUID;
        this.classes = classes;
    }

    public User(UUID studentUUID) {
        this.id = studentUUID;
        this.classes = new ArrayList<>();
    }

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public List<ClassInfo> getClasses() {
        return classes;
    }

    public void addClass(UUID classUUID, String className, UserRoles userRoles) {
        classes.add(new ClassInfo(classUUID, className, userRoles));
    }

    // Inner class to represent class information
    public static class ClassInfo {
        private UUID classUUID;
        private String className;
        private UserRoles userRole;

        public ClassInfo(UUID classUUID, String className, UserRoles userRole) {
            this.classUUID = classUUID;
            this.className = className;
            this.userRole = userRole;
        }

        public ClassInfo() {
        }

        public UUID getClassUUID() {
            return classUUID;
        }

        public String getClassName() {
            return className;
        }

        public void setClassUUID(UUID classUUID) {
            this.classUUID = classUUID;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
}