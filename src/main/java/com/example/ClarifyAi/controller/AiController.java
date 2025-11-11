package com.example.ClarifyAi.controller;

import com.example.ClarifyAi.dto.AiResponse;
import com.example.ClarifyAi.dto.PromptRequest;
import com.example.ClarifyAi.service.AiService;
import com.example.ClarifyAi.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AiController {

    private final AiService aiService;
    private final OpenAiChatModel chatModel;
    private final ValidationService validationService;

    @PostMapping
    public AiResponse handlePrompt(@RequestBody PromptRequest promptRequest) {
        validationService.checkRequest(promptRequest);
        Prompt prompt = aiService.getPrompt(promptRequest);
        String response = chatModel.call(prompt).getResult().getOutput().getText();
        validationService.checkResponse(response);
        return new AiResponse(response);
    }

}
