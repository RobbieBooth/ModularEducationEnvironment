package org.robbie.modulareducationenvironment.classes.datatype.contoller;

import org.robbie.modulareducationenvironment.authentication.UserRoles;
import org.robbie.modulareducationenvironment.classes.datatype.AvailableQuiz;
import org.robbie.modulareducationenvironment.classes.datatype.EditClassRequest;
import org.robbie.modulareducationenvironment.classes.datatype.QuizInfo;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleConfig;
import org.robbie.modulareducationenvironment.questionBank.Question;
import org.robbie.modulareducationenvironment.questionBank.Quiz;
import org.robbie.modulareducationenvironment.questionBank.QuizRepository;
import org.robbie.modulareducationenvironment.settings.dataTypes.QuizSettings;
import org.robbie.modulareducationenvironment.settings.dataTypes.Tuple;
import org.robbie.modulareducationenvironment.settings.dataTypes.questionSettings.settings.BaseSetting;
import org.robbie.modulareducationenvironment.userManagement.User;
import org.robbie.modulareducationenvironment.userManagement.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Add QuizInfo to the class, if the class does not exist then an error is thrown.
     * @param id the ID of the class to update
     * @param quizInfo the QuizInfo to be added to the class
     * @return the updated Class object
     */
    public Class updateClass(UUID id, QuizInfo quizInfo) throws IllegalArgumentException{
        return classRepository.findById(id).map(existingClass -> {
            // Add the new QuizInfo to the class
            List<QuizInfo> quizzes = existingClass.getQuizzes();
            if (quizzes == null) {
                quizzes = new ArrayList<>();
            }
            quizzes.add(quizInfo);
            existingClass.setQuizzes(quizzes);

            // Save the updated class
            return classRepository.save(existingClass);
        }).orElseThrow(() -> {
            // If the class does not exist, throw an exception
            throw new IllegalArgumentException("Class with ID " + id + " does not exist.");
        });
    }

    /**
     * Add AvailableQuiz to the class, if it already exists it updates it. If the class does not exist then an error is thrown.
     * @param id the ID of the class to update
     * @return the updated Class object
     */
    public Class updateClassWithAvailableQuiz(UUID id, AvailableQuiz availableQuiz) throws IllegalArgumentException{
        return classRepository.findById(id).map(existingClass -> {
            // Add the new QuizInfo to the class
            List<AvailableQuiz> quizzes = existingClass.getAvailableQuizzes();
            if (quizzes == null) {
                quizzes = new ArrayList<>();
            }
            boolean found = false;
            for (int i = 0; i < quizzes.size(); i++) {
                if (quizzes.get(i).getId().equals(availableQuiz.getId())) {
                    AvailableQuiz existingQuiz = quizzes.get(i);

                    //Update new quiz with previous attempts
                    availableQuiz.setStudentAttempts(existingQuiz.getStudentAttempts());

                    quizzes.set(i, availableQuiz); // Replace the existing quiz
                    found = true;
                    break;
                }
            }

            if(!found) {
                quizzes.add(availableQuiz);
            }

            existingClass.setAvailableQuizzes(quizzes);

            // Save the updated class
            return classRepository.save(existingClass);
        }).orElseThrow(() -> {
            // If the class does not exist, throw an exception
            throw new IllegalArgumentException("Class with ID " + id + " does not exist.");
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

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private ModuleConfig moduleConfig;


    /**
     * Updates the existing class by its id else it creates a default one by that id
     * @param id
     * @return
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<Class> editClass(@PathVariable UUID id, @RequestBody EditClassRequest request) {
        Class updatedClass = classService.updateClass(id, request.getClassName(), request.getClassDescription(), request.getEducators(), request.getStudents());

        // Check if the updated class is null
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

    // Save settings for a quiz
    @PostMapping("/{classID}/quiz/setting/save")
    public ResponseEntity<?> saveQuizSettings(@PathVariable UUID classID, @RequestBody QuizSettings settings) {
        //TODO check class exists before attempting save
        UUID newVersionID = UUID.randomUUID();
        List<Question> questions = new ArrayList<>();
        settings.getQuestions().forEach((key, value) -> {
            questions.add(new Question(value.getFirst(), key, value.getSecond()));
        });

        settings.getNewQuestions().forEach(value -> {
            questions.add(new Question(value.getFirst(), UUID.randomUUID(), value.getSecond()));
        });

        Quiz newQuiz = quizRepository.save(new Quiz(newVersionID, settings.getQuizUUID(), classID, questions, settings.getQuizSetting()));

        //Create Quiz on class:
        QuizInfo quizInfo = QuizInfo.createQuizInfo(newQuiz);
        try{
            classService.updateClass(classID, quizInfo);//THROWS if class does not exist
        } catch (IllegalArgumentException e) {
            // Handle exception by returning a meaningful error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Class with ID " + classID + " does not exist.");
        }


        return ResponseEntity.ok(getQuizSettings(newQuiz));
    }

    @PostMapping("/{classID}/quiz/available/save")
    public ResponseEntity<?> makeQuizAvailable(@PathVariable UUID classID, @RequestBody QuizAvailabilityRequest request) {

        //Get quizInfo
        QuizInfo quizInfo;
        if(request.getUseLatestVersion()){
            Optional<Quiz> optionalQuiz = quizRepository.findFirstByQuizUUIDOrderByCreatedAtDesc(request.getQuizID());
            if(optionalQuiz.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: Quiz with quiz ID " + request.getQuizID() + " not found.");
            }
            quizInfo = QuizInfo.createQuizInfo(optionalQuiz.get());
            quizInfo.setVersionID(Optional.empty());
        }else{
            if(request.getVersionID().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: For latest version the versionID field is required but was not provided.");
            }
            UUID versionID = request.getVersionID().get();
            Optional<Quiz> optionalQuiz = quizRepository.findById(versionID);
            if(optionalQuiz.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: Quiz with version ID " + versionID + " not found.");
            }
            quizInfo = QuizInfo.createQuizInfo(optionalQuiz.get());
            quizInfo.setVersionID(Optional.of(versionID));
        }

        AvailableQuiz availableQuiz = new AvailableQuiz(
                request.getId(),
                quizInfo,
                request.getStartTime(),
                request.getCloseTime(),
                request.getStudentsAvailableTo(),
                request.getUseLatestVersion(),
                new ArrayList<>(),//since no students have attempted it yet - although if quiz exists this will need to be updated to all attempts
                request.getInstantResult(),
                request.getMaxAttemptCount()
        );

        Class newClass;
        try{
            newClass = classService.updateClassWithAvailableQuiz(classID, availableQuiz);//THROWS if class does not exist
        } catch (IllegalArgumentException e) {
            // Handle exception by returning a meaningful error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Class with ID " + classID + " does not exist.");
        }
        return ResponseEntity.ok(newClass);
    }


    @GetMapping({"/{classID}/quiz/setting/{quizID}", "/{classID}/quiz/setting/"})
    public ResponseEntity<QuizSettings> getQuizSettings(@PathVariable String classID, @PathVariable(required = false) String quizID) {
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

class QuizAvailabilityRequest {
    private UUID id;
    private UUID quizID;
    private Optional<UUID> versionID;
    private Boolean useLatestVersion;
    private Optional<List<UUID>> studentsAvailableTo;
    private Optional<Long> startTime;
    private Optional<Long> closeTime;
    private Optional<Integer> maxAttemptCount;
    private Boolean instantResult;

    public QuizAvailabilityRequest() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getQuizID() {
        return quizID;
    }

    public void setQuizID(UUID quizID) {
        this.quizID = quizID;
    }

    public Optional<UUID> getVersionID() {
        return versionID;
    }

    public void setVersionID(Optional<UUID> versionID) {
        this.versionID = versionID;
    }

    public Boolean getUseLatestVersion() {
        return useLatestVersion;
    }

    public void setUseLatestVersion(Boolean useLatestVersion) {
        this.useLatestVersion = useLatestVersion;
    }

    public Optional<List<UUID>> getStudentsAvailableTo() {
        return studentsAvailableTo;
    }

    public void setStudentsAvailableTo(Optional<List<UUID>> studentsAvailableTo) {
        this.studentsAvailableTo = studentsAvailableTo;
    }

    public Optional<Long> getStartTime() {
        return startTime;
    }

    public void setStartTime(Optional<Long> startTime) {
        this.startTime = startTime;
    }

    public Optional<Long> getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Optional<Long> closeTime) {
        this.closeTime = closeTime;
    }

    public Optional<Integer> getMaxAttemptCount() {
        return maxAttemptCount;
    }

    public void setMaxAttemptCount(Optional<Integer> maxAttemptCount) {
        this.maxAttemptCount = maxAttemptCount;
    }

    public Boolean getInstantResult() {
        return instantResult;
    }

    public void setInstantResult(Boolean instantResult) {
        this.instantResult = instantResult;
    }
}