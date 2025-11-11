package com.example.ClarifyAi.service;


import com.example.ClarifyAi.dto.PromptRequest;
import com.example.ClarifyAi.exception.NotValidRequestException;
import com.example.ClarifyAi.exception.NullResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationService {

    public void checkRequest(PromptRequest promptRequest) {
        var request = Optional.ofNullable(promptRequest)
                .orElseThrow(() -> new NotValidRequestException("Request body is null."));

        Optional.ofNullable(request.text())
                .orElseThrow(() -> new NotValidRequestException("Text cannot be null."));

        Optional.ofNullable(request.action())
                .orElseThrow(() -> new NotValidRequestException("Action cannot be null."));

        checkText(request.text());
    }


    public void checkResponse(String response) {
        if (response == null) {
            throw new NullResponseException("The response is null.");
        }
    }

    private void checkText(String text) {
        if (countWords(text)>1000) {
            throw new NotValidRequestException("The entered text is too long (" + countWords(text) + " words). Use max 1000 words.");
        } else if (countWords(text) == 0) {
            throw new NotValidRequestException("Text cannot be empty.");
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
