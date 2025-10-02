package com.xumtech.chatbot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "QA_PAIRS")
public class QAPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String question;

    @Column(nullable = false, length = 1000)
    private String answer;

}
