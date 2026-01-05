package com.namanjain.llmchatbot.controller;

import com.namanjain.llmchatbot.dto.ChatRequest;
import com.namanjain.llmchatbot.dto.ChatResponse;
import com.namanjain.llmchatbot.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatService.process(request);
    }
}
