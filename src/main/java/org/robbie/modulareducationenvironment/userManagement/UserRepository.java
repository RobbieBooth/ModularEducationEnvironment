package org.robbie.modulareducationenvironment.userManagement;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User, UUID> {

    public long count();
    Optional<User> findById(String id);

}
