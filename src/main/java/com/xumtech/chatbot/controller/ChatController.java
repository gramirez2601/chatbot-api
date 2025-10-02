package com.xumtech.chatbot.controller;

import com.xumtech.chatbot.dto.ChatRequest;
import com.xumtech.chatbot.dto.ChatResponse;
import com.xumtech.chatbot.service.IChatService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes the chatbot functionality via a public API.
 */
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final IChatService chatService;

    /**
     * Constructor for dependency injection of the chat service.
     *
     * @param chatService The chat service implementation.
     */
    public ChatController(IChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Handles the incoming chat requests from the user.
     *
     * @param request The ChatRequest DTO containing the user's message.
     * @return A ChatResponse DTO containing the bot's reply.
     */
    @PostMapping
    public ChatResponse handleChat(@RequestBody ChatRequest request) {
        return chatService.processMessage(request);
    }
}