package com.xumtech.chatbot.repository;

import com.xumtech.chatbot.entity.QAPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing QAPair entities.
 */
@Repository
public interface IQAPairRepository extends JpaRepository<QAPair, Long> {

    /**
     * Find a QAPair by its question.
     * @param question the question to search for
     * @return an Optional containing the found QAPair, or empty if not found
     */
    Optional<QAPair> findByQuestion(String question);

}