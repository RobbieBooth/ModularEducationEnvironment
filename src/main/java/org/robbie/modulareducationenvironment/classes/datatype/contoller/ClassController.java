package org.robbie.modulareducationenvironment.classes.datatype.contoller;

import org.robbie.modulareducationenvironment.authentication.UserRoles;
import org.robbie.modulareducationenvironment.classes.datatype.*;
import org.robbie.modulareducationenvironment.classes.datatype.Class;
import org.robbie.modulareducationenvironment.jwt.JwtAuthenticationFilter;
import org.robbie.modulareducationenvironment.moduleHandler.ModuleConfig;
import org.robbie.modulareducationenvironment.questionBank.*;
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

import java.util.*;
import java.util.stream.Collectors;

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
            Class newClass = new Class(className, classDescription, educators, students, existingClass.getQuizzes(), existingClass.getAvailableQuizzes());
            newClass.setId(existingClass.getId());

            Class updatedClass = classRepository.save(newClass);

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

    public boolean removeAvailableQuiz(UUID classId, UUID availableQuizId) {
        Optional<Class> optionalClass = classRepository.findById(classId);

        if (optionalClass.isPresent()) {
            Class classObj = optionalClass.get();

            //Remove the AvailableQuiz with the given ID
            boolean removed = classObj.getAvailableQuizzes().removeIf(quiz -> quiz.getId().equals(availableQuizId));

            if (removed) {
                //Save updated class
                classRepository.save(classObj);
                return true;
            }
        }
        //Return false if class or quiz not found
        return false;
    }

    /**
     * Add `SampleStudentAttempt` to the classes availableQuiz, if the SampleStudentAttempt already exists it updates it. If the class does not exist or the available quiz then an error is thrown.
     * @param classID The id of the class that contains the available quiz and of which the sample attempt will be added it
     * @param availableQuizUUID The available quiz id which the student attempt will be added to. If it doesn't exist error is thrown.
     * @param sampleStudentAttempt The student attempt to add to the availableQuiz
     * @return the updated Class object
     */
    public Class updateClassWithStudentAttempt(UUID classID, UUID availableQuizUUID, SampleStudentAttempt sampleStudentAttempt) throws IllegalArgumentException{
        return classRepository.findById(classID).map(existingClass -> {
            // Add the new QuizInfo to the class
            List<AvailableQuiz> quizzes = existingClass.getAvailableQuizzes();
            if (quizzes == null) {
                quizzes = new ArrayList<>();
            }
            boolean found = false;
            for (int i = 0; i < quizzes.size(); i++) {
                if (quizzes.get(i).getId().equals(availableQuizUUID)) {
                    AvailableQuiz existingQuiz = quizzes.get(i);

                    List<SampleStudentAttempt> studentAttempts = existingQuiz.getStudentAttempts();

                    Optional<SampleStudentAttempt> optionalSampleStudentAttempt = findSampleStudentAttempt(sampleStudentAttempt.getStudentAttemptId(), studentAttempts);
                    if(optionalSampleStudentAttempt.isPresent()) {
                        SampleStudentAttempt existingSampleStudentAttempt = optionalSampleStudentAttempt.get();
                        int index = studentAttempts.indexOf(existingSampleStudentAttempt);
                        studentAttempts.set(index, sampleStudentAttempt); // Replace the old attempt with the new one
                    }else{
                        studentAttempts.add(sampleStudentAttempt);
                    }

                    existingQuiz.setStudentAttempts(studentAttempts);
                    quizzes.set(i, existingQuiz); // Replace the existing quiz
                    found = true;
                    break;
                }
            }

            if(!found) {
                throw new IllegalArgumentException("Available Quiz with id " + availableQuizUUID + " not found.");
            }

            existingClass.setAvailableQuizzes(quizzes);

            // Save the updated class
            return classRepository.save(existingClass);
        }).orElseThrow(() -> {
            // If the class does not exist, throw an exception
            throw new IllegalArgumentException("Class with ID " + classID + " does not exist.");
        });
    }

    /**
     * Looks for the attempt of sampleAttemptID and if it is not found it returns an empty.
     * @param sampleAttemptID
     * @param allAttempts
     * @return
     */
    public Optional<SampleStudentAttempt> findSampleStudentAttempt(UUID sampleAttemptID, List<SampleStudentAttempt> allAttempts) {
        return allAttempts.stream().filter(sampleStudentAttempt -> sampleStudentAttempt.getStudentAttemptId().equals(sampleAttemptID)).findFirst();
    }

    public Optional<Class> getClassById(UUID id) {
        return classRepository.findById(id);
    }

    /**
     * Search all the classes to see if the user is in it and return the list of their classes
     * @param userId
     * @return
     */
    public List<Class> getClassesByUserId(UUID userId) {
        return classRepository.findAll()
                .stream()
                .filter(classObj -> classObj.isUserInClass(userId))
                .collect(Collectors.toList());
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
    private QuizAttemptRepository quizAttemptRepository;

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


    /**
     * Get the classes for that user
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<?> getUsersClasses() {
        //Get requesting users id
        UUID userUUID;
        try{
            userUUID = JwtAuthenticationFilter.getUserUUID();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.ok(classService.getClassesByUserId(userUUID));
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

        settings.getNewQuestions().forEach((key, value) -> {
            questions.add(new Question(value.getFirst(), key, value.getSecond()));
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

    //Get class
    //check student is on the class
    //check that available quiz is on that class
    //check that student has not made more than the max amount of attempts at quiz
    //create student attempt, add it to the class and return the attempt id
    @PostMapping("/{classID}/quiz/available/start/{availableQuizUUID}")
    public ResponseEntity<?> startAvailableQuiz(@PathVariable UUID classID, @PathVariable UUID availableQuizUUID) {
        //Get requesting users id
        UUID userUUID;
        try{
            userUUID = JwtAuthenticationFilter.getUserUUID();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        //Get class
        Optional<Class> optionalClass = classService.getClassById(classID);
        if(optionalClass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Class with id " + classID.toString() + " not found.");
        }
        Class currentClass = optionalClass.get();

        //check that user has valid permissions ie they are an educator or a student who owns the quiz
        List<UUID> educators = currentClass.getEducators();
        List<UUID> students = currentClass.getStudents();
        //get users role
        UserRoles userRole = null;
        if (educators.contains(userUUID)){
            userRole = UserRoles.EDUCATOR;
        }else if (students.contains(userUUID)){
            userRole = UserRoles.STUDENT;
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: User does not belong to class");
        }


        //get the attempt and return it.
        List<AvailableQuiz> availableQuizzes = currentClass.getAvailableQuizzes().stream().filter(availableQuiz -> availableQuiz.getId().equals(availableQuizUUID)).collect(Collectors.toList());
        if(availableQuizzes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: No available quiz with id "+availableQuizUUID+" found.");
        }
        AvailableQuiz availableQuiz = availableQuizzes.get(0);

        //TODO add in check for expire time and start time
        Optional<Integer> optionalMaxAttempts = availableQuiz.getMaxAttemptCount();
        //If empty option then we can return quiz to student infinite times otherwise it would be the max attempt count
        if(optionalMaxAttempts.isPresent()){
            Integer maxAttempts = optionalMaxAttempts.get();
            long studentAttemptCount = availableQuiz.getStudentAttempts().stream().filter(sampleStudentAttempt -> sampleStudentAttempt.getStudentID().equals(userUUID)).count();
            if(studentAttemptCount >= maxAttempts){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Max attempts for quiz reached, "+ studentAttemptCount +"/"+ maxAttempts +" attempts made.");
            }
        }

        //Get quiz to create student attempt
        QuizInfo quizInfo = availableQuiz.getQuizInfo();
        Optional<UUID> quizVersion = quizInfo.getVersionID();
        //if quizVersion is specified we use it otherwise we use latest quiz version
        Quiz quiz;
        if(quizVersion.isPresent()){
            quiz = quizRepository.findById(quizVersion.get()).orElse(null);
            if(quiz == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Available quiz of id "+availableQuizUUID+" was found but its reliant quiz of version id "+quizVersion+"was not found.");
            }
        }else{
            quiz = quizRepository.findFirstByQuizUUIDOrderByCreatedAtDesc(quizInfo.getQuizID()).orElse(null);
            if(quiz == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Available quiz of id "+availableQuizUUID+" was found but its reliant quiz of quiz id "+quizInfo.getQuizID()+"was not found.");
            }
        }

        //create attempt and add it to class
        studentQuizAttempt studentQuizAttempt = quiz.createStudentQuizAttempt(userUUID, availableQuizUUID);
        quizAttemptRepository.save(studentQuizAttempt);

        //Update class with the new attempt:
        SampleStudentAttempt sampleStudentAttempt = new SampleStudentAttempt(
                studentQuizAttempt.getStudentQuizAttemptUUID(),
                studentQuizAttempt.getQuizTemplateUUID(),
                studentQuizAttempt.getQuizVersionRef(),
                studentQuizAttempt.getUserUUID(),
                Optional.empty(),
                Optional.empty());
        classService.updateClassWithStudentAttempt(classID, availableQuizUUID, sampleStudentAttempt);

        //return
        return ResponseEntity.ok(studentQuizAttempt);
    }

    @DeleteMapping("/{classID}/quiz/available/{availableQuizUUID}")
    public ResponseEntity<?> deleteAvailableQuiz(@PathVariable UUID classID, @PathVariable UUID availableQuizUUID) {
        //Get requesting users id
        UUID userUUID;
        try{
            userUUID = JwtAuthenticationFilter.getUserUUID();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        //Get class
        Optional<Class> optionalClass = classService.getClassById(classID);
        if(optionalClass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Class with id " + classID.toString() + " not found.");
        }
        Class currentClass = optionalClass.get();

        //check that user has valid permissions ie they are an educator
        List<UUID> educators = currentClass.getEducators();
        //get users role
        UserRoles userRole = null;
        if (educators.contains(userUUID)){
            userRole = UserRoles.EDUCATOR;
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Does not have sufficient permissions");
        }

        //remove the class
        return ResponseEntity.ok(classService.removeAvailableQuiz(classID, availableQuizUUID));
    }

    @GetMapping("/{classID}/quiz/available/{availableQuizUUID}/{studentAttemptID}")
    public ResponseEntity<?> getStudentQuizAttempt(@PathVariable UUID classID, @PathVariable UUID availableQuizUUID, @PathVariable UUID studentAttemptID) {
        //Get requesting users id
        UUID userUUID;
        try{
            userUUID = JwtAuthenticationFilter.getUserUUID();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        //Get class
        Optional<Class> optionalClass = classService.getClassById(classID);
        if(optionalClass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Class with id " + classID.toString() + " not found.");
        }
        Class currentClass = optionalClass.get();

        //check that user has valid permissions ie they are an educator or a student who owns the quiz
        List<UUID> educators = currentClass.getEducators();
        List<UUID> students = currentClass.getStudents();
        //get users role
        UserRoles userRole = null;
        if (educators.contains(userUUID)){
            userRole = UserRoles.EDUCATOR;
        }else if (students.contains(userUUID)){
            userRole = UserRoles.STUDENT;
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: User does not belong to class");
        }


        //get the attempt and return it.
        List<AvailableQuiz> availableQuizzes = currentClass.getAvailableQuizzes().stream().filter(availableQuiz -> availableQuiz.getId().equals(availableQuizUUID)).collect(Collectors.toList());
        if(availableQuizzes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: No available quiz with id "+availableQuizUUID+" found.");
        }
        AvailableQuiz availableQuiz = availableQuizzes.get(0);

        //Get the sample student attempts
        List<SampleStudentAttempt> studentQuizAttempts = availableQuiz.getStudentAttempts().stream().filter(sampleStudentAttempt -> sampleStudentAttempt.getStudentAttemptId().equals(studentAttemptID)).collect(Collectors.toList());
        if(studentQuizAttempts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: No student attempt with id "+studentAttemptID+" found.");
        }
        SampleStudentAttempt sampleStudentAttempt = studentQuizAttempts.get(0);
        //check if it's a student that they own the quiz:
        if(userRole.equals(UserRoles.STUDENT) && !sampleStudentAttempt.getStudentID().equals(userUUID)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: You do not own this attempt");
        }

        //get students attempt and return it
        Optional<studentQuizAttempt> optionalStudentQuizAttempt = quizAttemptRepository.findById(sampleStudentAttempt.getStudentAttemptId());
        if(optionalStudentQuizAttempt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: No student attempt with id "+studentAttemptID+" found in database.");
        }
        return ResponseEntity.ok(optionalStudentQuizAttempt.get());
    }

    @GetMapping("/{classID}/quiz/available/{availableQuizUUID}")
    public ResponseEntity<?> getAvailableQuizSettings(@PathVariable UUID classID, @PathVariable UUID availableQuizUUID) {
        //Get requesting users id
        UUID userUUID;
        try{
            userUUID = JwtAuthenticationFilter.getUserUUID();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        //Get class
        Optional<Class> optionalClass = classService.getClassById(classID);
        if(optionalClass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Class with id " + classID.toString() + " not found.");
        }
        Class currentClass = optionalClass.get();

        //check that user has valid permissions
        List<UUID> educators = currentClass.getEducators();
        List<UUID> students = currentClass.getStudents();

        //User does not exist on class
        if(!educators.contains(userUUID) && !students.contains(userUUID)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: User does not belong to class");
        }

        //check that available quiz is on that class
        List<AvailableQuiz> availableQuizzes = currentClass.getAvailableQuizzes().stream().filter(availableQuiz -> availableQuiz.getId().equals(availableQuizUUID)).collect(Collectors.toList());
        if(availableQuizzes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: No available quiz with id "+availableQuizUUID+" found.");
        }

        //get available quiz and return it
        AvailableQuiz availableQuiz = availableQuizzes.get(0);
        return ResponseEntity.ok(availableQuiz);
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
                new HashMap<>()
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