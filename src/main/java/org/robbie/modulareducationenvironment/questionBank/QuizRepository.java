package org.robbie.modulareducationenvironment.questionBank;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizRepository extends MongoRepository<Quiz, UUID> {

//    @Query("{parentQuizUUID:'?0'}")
//    List<studentQuizAttempt> findQuizAttemptByParentQuizUUID(String parentQuizUUID);
    Optional<Quiz> findFirstByQuizUUIDOrderByCreatedAtDesc(UUID quizUUID);

    public long count();
}
