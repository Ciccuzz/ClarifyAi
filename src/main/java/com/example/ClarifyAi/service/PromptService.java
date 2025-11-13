package com.example.ClarifyAi.service;

import com.example.ClarifyAi.dto.PromptRequest;
import com.example.ClarifyAi.mapper.ActionMapper;
import com.example.ClarifyAi.model.Length;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.ClarifyAi.model.ActionEnum.*;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final ActionMapper actionMapper;

    public Prompt getPrompt(PromptRequest promptRequest) {
        String systemPrompt = getSystemPrompt(promptRequest);
        return new Prompt(List.of(new SystemMessage(systemPrompt), new UserMessage(promptRequest.text())));
    }

    private String getSystemPrompt(PromptRequest promptRequest) {
        return getAction(promptRequest.action(), promptRequest.personalizedAction()) + getLength(promptRequest.maxWords(), promptRequest.length());
    }

    private String getAction(String action, String personalizedAction) {
        return switch (actionMapper.toEnum(action)) {
            case SUMMARY -> SUMMARY.string;
            case SIMPLIFY -> SIMPLIFY.string;
            case TRANSLATE_IT -> TRANSLATE_IT.string;
            case TRANSLATE_EN -> TRANSLATE_EN.string;
            case PERSONALIZED -> personalizedAction;
        };
    }

    private String getLength(Integer maxWords, Length length) {
        if (maxWords != null && maxWords > 0) {
            return  " Usa massimo " + maxWords + " parole.";
        } else {
            return  " Usa massimo " + length.words + " parole.";
        }
    }
}
