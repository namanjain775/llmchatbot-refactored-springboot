package com.namanjain.llmchatbot.service;

import com.namanjain.llmchatbot.client.OpenAIClient;
import com.namanjain.llmchatbot.dto.ChatRequest;
import com.namanjain.llmchatbot.dto.ChatResponse;
import com.namanjain.llmchatbot.util.PromptBuilder;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final OpenAIClient openAIClient;

    public ChatService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public ChatResponse process(ChatRequest request) {

        // 1️⃣ Build domain-specific prompt
        String prompt = PromptBuilder.buildSupportPrompt(
                request.getMessage()
        );

        // 2️⃣ Delegate to OpenAI client
        // (client returns enriched ChatResponse: reply + confidence + TokenUsage)
        return openAIClient.ask(prompt);
    }
}
