package org.robbie.modulareducationenvironment.userManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

//    // Get a user by their UUID
//    @GetMapping("/{id}")
//    public Optional<User> getUserById(@PathVariable UUID id) {
//        User user = userRepository.findById(id).orElse(null);
//        return Optional.ofNullable(user);
//    }

    @GetMapping("/{userUUID}")
    public ResponseEntity<User> getUserById(@PathVariable String userUUID) {
        UUID userUUIDValue = UUID.fromString(userUUID);
        Optional<User> user = userRepository.findById(userUUIDValue);
        List<User> users = userRepository.findAll();
        if(!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.get());
    }

    // Add a new user
    @PostMapping("/{userUUID}")
    public User addUser(@PathVariable String userUUID) {
        UUID uuid = UUID.fromString(userUUID);
        User newUser = new User(uuid, new ArrayList<>());
        return userRepository.save(newUser);
    }

    @PostMapping("/createmany")
    public List<User> addUser(@RequestBody List<String> userUUIDs) {
        List<User> users = new ArrayList<>();
        for(String userUUID : userUUIDs) {
            UUID uuid = UUID.fromString(userUUID);
            User newUser = new User(uuid, new ArrayList<>());
            users.add(userRepository.save(newUser));
        }
        return users;
    }

    // Add a new user
//    @PostMapping
//    public User addUser(@RequestBody User user) {
//        return userRepository.save(user);
//    }

    // Get all users
    @GetMapping
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}

