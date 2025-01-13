package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends MongoRepository<Student, UUID> {

    public long count();
}
