package com.xumtech.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chat response containing the chatbot's reply.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    /**
     * The reply message from the chatbot.
     */
    private String reply;
}