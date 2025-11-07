package com.example.ClarifyAi.service;

import com.example.ClarifyAi.dto.PromptRequest;
import com.example.ClarifyAi.exception.NotValidTextException;
import com.example.ClarifyAi.exception.NullResponseException;
import com.example.ClarifyAi.exception.UnknownActionException;
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
public class AiService {

    private final ActionMapper actionMapper;

    public Prompt getPrompt(PromptRequest promptRequest) {
        if (!textIsOk(promptRequest.text())) {
            throw new NotValidTextException("The entered text is too long (" + countWords(promptRequest.text()) + " words). Use max 1000 words");
        }
        String systemPrompt = getSystemPrompt(promptRequest.action(), promptRequest.maxWords(), promptRequest.length());

        return new Prompt(List.of(new SystemMessage(systemPrompt), new UserMessage(promptRequest.text())));
    }

    public void checkResponse(String response) {
        if (response == null) {
            throw new NullResponseException("The response is null.");
        }
    }

    private String getSystemPrompt(String action, Integer maxWords, Length length) {
        String systemPrompt = switch (actionMapper.toEnum(action)) {
            case SUMMARY -> SUMMARY.string;
            case SIMPLIFY -> SIMPLIFY.string;
            case TRANSLATE_IT -> TRANSLATE_IT.string;
            case TRANSLATE_EN -> TRANSLATE_EN.string;
        };

        if (maxWords != null && maxWords > 0) {
            systemPrompt += " Usa massimo " + maxWords + " parole.";
        } else {
            systemPrompt += " Usa massimo " + length.words + " parole.";
        }

        return systemPrompt;
    }

    private int countWords(String text) {
        if (text == null || text.isBlank()) {
            throw new NotValidTextException("Text cannot be null or empty.");
        }
        String[] words = text.trim().split("\\s+");
        return words.length;
    }

    private boolean textIsOk(String text) {
        int wordsInText = countWords(text);
        return wordsInText <= 1000;
    }



}
