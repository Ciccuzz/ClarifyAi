package com.example.ClarifyAi.utility;


import com.example.ClarifyAi.dto.ChatRequest;
import com.example.ClarifyAi.dto.StartSessionRequest;
import com.example.ClarifyAi.exception.NotValidChatRequestException;
import com.example.ClarifyAi.exception.NotValidStartSessionRequest;
import com.example.ClarifyAi.exception.NullResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Validator {

    public void checkChatRequest(ChatRequest chatRequest) {
        var request = Optional.ofNullable(chatRequest)
                .orElseThrow(() -> new NotValidChatRequestException("Request body is null."));

        Optional.ofNullable(request.text())
                .orElseThrow(() -> new NotValidChatRequestException("Text cannot be null."));

        Optional.ofNullable(request.sessionId())
                .orElseThrow(() -> new NotValidChatRequestException("Session Id cannot be null."));

        checkText(request.text());
    }

    public void checkChatResponse(String response) {
        if (response == null) {
            throw new NullResponseException("The response is null.");
        }
    }

    public void checkStartSessionRequest(StartSessionRequest startSessionRequest) {
        var request = Optional.ofNullable(startSessionRequest)
                .orElseThrow(() -> new NotValidStartSessionRequest("Start Session Request is null."));

        Optional.ofNullable(request.context())
                .orElseThrow(() -> new NotValidStartSessionRequest("Context cannot be null."));
    }

    private void checkText(String text) {
        if (countWords(text) > 1000) {
            throw new NotValidChatRequestException("The entered text is too long (" + countWords(text) + " words). Use max 1000 words.");
        } else if (countWords(text) == 0) {
            throw new NotValidChatRequestException("Text cannot be empty.");
        }
    }

    private int countWords(String text) {
        text = text.trim();
        if (text.isEmpty()) {
            return 0;
        }
        return text.split("\\s+").length;
    }
}
