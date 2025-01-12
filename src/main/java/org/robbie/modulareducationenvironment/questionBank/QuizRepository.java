package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface QuizRepository extends MongoRepository<Quiz, UUID> {

//    @Query("{parentQuizUUID:'?0'}")
//    List<studentQuizAttempt> findQuizAttemptByParentQuizUUID(String parentQuizUUID);

    public long count();
}
