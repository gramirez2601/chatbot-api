package com.xumtech.chatbot.service;

import com.xumtech.chatbot.dto.ChatRequest;
import com.xumtech.chatbot.dto.ChatResponse;
import com.xumtech.chatbot.entity.QAPair;
import com.xumtech.chatbot.repository.IQAPairRepository;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

/**
 * Main implementation of the chatbot's business logic.
 */
@Service
public class ChatServiceImpl implements IChatService {

    private final IQAPairRepository qaPairRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param qaPairRepository The repository for accessing question and answer data.
     */
    public ChatServiceImpl(IQAPairRepository qaPairRepository) {
        this.qaPairRepository = qaPairRepository;
    }

    /**
     * {@inheritDoc}
     * This implementation searches for an answer in the database based on the user's question.
     * If no match is found, it returns a default message.
     */
    @Override
    public ChatResponse processMessage(ChatRequest request) {
        String userQuestion = request.getMessage().toLowerCase().trim();
        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

        QAPair bestMatch = null;
        int minDistance = Integer.MAX_VALUE;

        // We retrieve all QAPairs from the database
        Iterable<QAPair> allPairs = qaPairRepository.findAll();

        // We look for the closest question using Levenshtein distance
        for (QAPair pair : allPairs) {
            int distance = levenshteinDistance.apply(userQuestion, pair.getQuestion());
            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = pair;
            }
        }

        // If we found a close enough match, we return the corresponding answer
        if (bestMatch != null && minDistance <= 3) {
            return new ChatResponse(bestMatch.getAnswer());
        } else {
            return new ChatResponse("Lo siento, no he entendido tu pregunta. ¿Puedes intentar de otra forma?");
        }
    }
}