package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuizAttemptRepository extends MongoRepository<studentQuizAttempt, UUID> {

//    @Query("{parentQuizUUID:'?0'}")
//    List<studentQuizAttempt> findQuizAttemptByParentQuizUUID(String parentQuizUUID);

    public long count();
}
