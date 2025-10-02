package com.xumtech.chatbot.repository;

import com.xumtech.chatbot.entity.QAPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing QAPair entities.
 */
@Repository
public interface IQAPairRepository extends JpaRepository<QAPair, Long> {

}