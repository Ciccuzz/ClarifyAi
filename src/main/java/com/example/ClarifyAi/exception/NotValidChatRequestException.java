package com.example.ClarifyAi.exception;

public class NotValidChatRequestException extends RuntimeException {
    public NotValidChatRequestException(String message) {
        super(message);
    }
}
