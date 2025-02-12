package org.robbie.modulareducationenvironment.classes.datatype.contoller;

import org.robbie.modulareducationenvironment.authentication.UserRoles;
import org.robbie.modulareducationenvironment.classes.datatype.EditClassRequest;
import org.robbie.modulareducationenvironment.questionBank.Quiz;
import org.robbie.modulareducationenvironment.settings.dataTypes.QuizSettings;
import org.robbie.modulareducationenvironment.userManagement.User;
import org.robbie.modulareducationenvironment.userManagement.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.robbie.modulareducationenvironment.classes.datatype.Class;

import java.util.*;

@Repository
interface ClassRepository extends MongoRepository<Class, UUID> {
}

@Service
class ClassService {
    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

//    public Class createDefaultClass(String className, String classDescription, List<UUID> educators, List<UUID> students) {
//        Class newClass = new Class(className, classDescription, educators, students, new ArrayList<>(), new ArrayList<>());
//        return classRepository.save(newClass);
//    }

//    public Optional<Class> updateClass(UUID id, Class updatedClass) {
//        if (classRepository.existsById(id)) {
//            updatedClass.setId(id);
//            return Optional.of(classRepository.save(updatedClass));
//        }
//        return Optional.empty();
//    }

    /**
     * Updates the existing class by its id else it creates a default one by that id
     * @param id
     * @param className
     * @param classDescription
     * @param educators
     * @param students
     * @return
     */
    public Class updateClass(UUID id, String className, String classDescription, List<UUID> educators, List<UUID> students) {
        return classRepository.findById(id).map(existingClass -> {
            existingClass.setClassName(className);
            existingClass.setClassDescription(classDescription);
            existingClass.setEducators(educators);
            existingClass.setStudents(students);
            Class updatedClass = classRepository.save(existingClass);

            //Update the users records with the classes new details
            updateUsersOnClassChange(existingClass, updatedClass);

            return updatedClass;
        }).orElseGet(() -> {
            // Create a new class if not found
            Class newClass = new Class(className, classDescription, educators, students, new ArrayList<>(), new ArrayList<>());
            newClass.setId(id);
            Class createdClass = classRepository.save(newClass); // Save and return the new class

            //Update the users records with the new classes details
            //The old class will be a basically empty class as we want to update all these user no matter what
            updateUsersOnClassChange(new Class("", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()), createdClass);

            return createdClass;
        });
    }

    public Optional<Class> getClassById(UUID id) {
        return classRepository.findById(id);
    }

    /**
     * Updated the users and educators of the classes user records with the current updated version.
     * This could be removing them or adding the class to the users record or updating the name.
     * This is done to ensure that when a user gets their record they do not need to request each of their classes and can just request the record.
     * @param oldClass
     * @param newClass
     */
    public void updateUsersOnClassChange(Class oldClass, Class newClass){
        UUID classID = newClass.getId();

        //Get the old students and educators and then update accordingly
        Set<UUID> oldEducators = new HashSet<>(oldClass.getEducators());
        Set<UUID> newEducators = new HashSet<>(newClass.getEducators());

        Set<UUID> oldStudents = new HashSet<>(oldClass.getStudents());
        Set<UUID> newStudents = new HashSet<>(newClass.getStudents());

        boolean hasClassNameUpdated = !oldClass.getClassName().equals(newClass.getClassName());
        boolean hasEducatorsUpdated = !oldEducators.equals(newEducators);
        boolean hasStudentsUpdated = !oldStudents.equals(newStudents);

        //No updates needed since nothing important has changed
        if(!hasClassNameUpdated && !hasEducatorsUpdated && !hasStudentsUpdated){
            return;
        }

        Map<UUID, User.ClassInfo> addedUserMap = new HashMap<>();

        if(hasEducatorsUpdated){
            Set<UUID> removedEducators = getRemovedUsers(oldEducators, newEducators);
            Set<UUID> addedEducators = getAddedUsers(oldEducators, newEducators);

            //Remove class from user records
            removeClassFromUsers(removedEducators, classID);

            //We add the educators and students at same time to stop a potential bug of educators moving to students or vice versa
            for (UUID uuid : addedEducators) {
                addedUserMap.put(uuid, new User.ClassInfo(classID, newClass.getClassName(), UserRoles.EDUCATOR));
            }
        }

        if(hasStudentsUpdated){
            Set<UUID> removedStudents = getRemovedUsers(oldStudents, newStudents);
            Set<UUID> addedStudents = getAddedUsers(oldStudents, newStudents);

            //remove class from user records
            removeClassFromUsers(removedStudents, classID);

            //We add the educators and students at same time to stop a potential bug of educators moving to students or vice versa
            for (UUID uuid : addedStudents) {
                addedUserMap.put(uuid, new User.ClassInfo(classID, newClass.getClassName(), UserRoles.STUDENT));
            }
        }

        //Add class to users
        addClassToUsers(addedUserMap);

        //Finally update all the leftover users with the new class name if its changed.
        if(hasClassNameUpdated){
            Set<UUID> unchangedUsers = new HashSet<>();
            Set<UUID> unchangedEducators = getUnchangedUsers(oldEducators, newEducators);
            Set<UUID> unchangedStudents = getUnchangedUsers(oldStudents, newStudents);
            unchangedUsers.addAll(unchangedEducators);
            unchangedUsers.addAll(unchangedStudents);

            updateClassNameOnUsers(unchangedUsers, classID, newClass.getClassName());
        }
    }

    private void removeClassFromUsers(Set<UUID> usersToHaveClassRemoved, UUID classUUID){
        for (UUID removedUserID : usersToHaveClassRemoved) {
            userRepository.findById(removedUserID).ifPresent(user -> {
                user.getClasses().removeIf(classInfo -> classInfo.getClassUUID().equals(classUUID));
                userRepository.save(user);
            });
        }
    }

    /**
     * Adds the Class Info to the user if they exist.
     * @param addClassToUsers
     */
    public void addClassToUsers(Map<UUID, User.ClassInfo> addClassToUsers){
        for (Map.Entry<UUID, User.ClassInfo> entry : addClassToUsers.entrySet()) {
            UUID userId = entry.getKey();
            User.ClassInfo classInfo = entry.getValue();

            userRepository.findById(userId).ifPresent(user -> {
                // Add the class info to the user's classes
                user.getClasses().add(classInfo);
                // Save the updated user
                userRepository.save(user);
            });
        }
    }

    public void updateClassNameOnUsers(Set<UUID> users, UUID classUUID, String className) {
        for (UUID userID : users) {
            userRepository.findById(userID).ifPresent(user -> {
                // Iterate through the user's classes to find the matching classUUID
                user.getClasses().stream()
                        .filter(classInfo -> classInfo.getClassUUID().equals(classUUID))
                        .findFirst()
                        .ifPresent(classInfo -> {
                            // Update the class name if the class is found
                            classInfo.setClassName(className);
                        });

                // Save the updated user
                userRepository.save(user);
            });
        }
    }


    private Set<UUID> getRemovedUsers(Set<UUID> oldUUIDs, Set<UUID> newUUIDs){
        // Find removed UUIDs
        Set<UUID> removed = new HashSet<>(oldUUIDs);
        removed.removeAll(newUUIDs);

        return removed;
    }

    private Set<UUID> getAddedUsers(Set<UUID> oldUUIDs, Set<UUID> newUUIDs){
        // Find added UUIDs
        Set<UUID> added = new HashSet<>( newUUIDs);
        added.removeAll(oldUUIDs);

        return added;
    }

    private Set<UUID> getUnchangedUsers(Set<UUID> oldUUIDs, Set<UUID> newUUIDs){
        // Find unchanged UUIDs
        Set<UUID> unchanged = new HashSet<>(oldUUIDs);
        unchanged.retainAll(newUUIDs);

        return unchanged;
    }


}

@RestController
@RequestMapping("/v1/api/class")
class ClassController {
    @Autowired
    private ClassService classService;


    /**
     * Updates the existing class by its id else it creates a default one by that id
     * @param id
     * @return
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<Class> editClass(@PathVariable UUID id, @RequestBody EditClassRequest request) {
        Class updatedClass = classService.updateClass(id, request.getClassName(), request.getClassDescription(), request.getEducators(), request.getStudents());

        // Check if the updated class is null (unlikely based on your implementation)
        if (updatedClass != null) {
            return ResponseEntity.ok(updatedClass); // Return the updated class with a 200 OK status
        }

        return ResponseEntity.notFound().build(); // Fallback in case something goes wrong
    }


    @GetMapping("/{id}")
    public ResponseEntity<Class> getClassById(@PathVariable UUID id) {
        return classService.getClassById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
