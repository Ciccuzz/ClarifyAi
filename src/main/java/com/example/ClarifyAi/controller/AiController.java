package com.example.ClarifyAi.controller;

import com.example.ClarifyAi.dto.ChatRequest;
import com.example.ClarifyAi.dto.ChatResponse;
import com.example.ClarifyAi.dto.StartSessionRequest;
import com.example.ClarifyAi.dto.StartSessionResponse;
import com.example.ClarifyAi.service.PromptService;
import com.example.ClarifyAi.session.SessionData;
import com.example.ClarifyAi.session.SessionStore;
import com.example.ClarifyAi.utility.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AiController {

    private final PromptService promptService;
    private final OpenAiChatModel chatModel;
    private final Validator validator;
    private final SessionStore sessionStore;

    @PostMapping("/start")
    public StartSessionResponse startSession(@RequestBody StartSessionRequest startSessionRequest) {
        validator.checkStartSessionRequest(startSessionRequest);
        String sessionId = sessionStore.createSession(startSessionRequest.context());
        return new StartSessionResponse(sessionId);
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest chatRequest) {

        validator.checkChatRequest(chatRequest);

        SessionData session = sessionStore.getSession(chatRequest.sessionId());

        if (session == null) {
            throw new IllegalArgumentException("Sessione non trovata: " + chatRequest.sessionId());
        }

        session.updateAccessTime();

        Prompt prompt = promptService.getPrompt(session, chatRequest.text());

        String aiResponse = chatModel.call(prompt).getResult().getOutput().getText();

        validator.checkChatResponse(aiResponse);

        session.getMessages().add("Utente: " + chatRequest.text());
        session.getMessages().add("AI: " + aiResponse);

        return new ChatResponse(aiResponse);
    }
}
