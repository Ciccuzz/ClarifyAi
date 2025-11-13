package com.example.ClarifyAi.controller;

import com.example.ClarifyAi.dto.AiResponse;
import com.example.ClarifyAi.dto.PromptRequest;
import com.example.ClarifyAi.service.PromptService;
import com.example.ClarifyAi.utility.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AiController {

    private final PromptService promptService;
    private final OpenAiChatModel chatModel;
    private final Validator validator;

    @PostMapping
    public Optional<AiResponse> handlePrompt(@RequestBody PromptRequest promptRequest) {
        validator.checkRequest(promptRequest);
        Prompt prompt = promptService.getPrompt(promptRequest);
        String response = chatModel.call(prompt).getResult().getOutput().getText();
        validator.checkResponse(response);
        return Optional.of(new AiResponse(response));
    }

}
