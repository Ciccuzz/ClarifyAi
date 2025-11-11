package com.example.ClarifyAi.exception;

public class NotValidRequestException extends RuntimeException {
    public NotValidRequestException(String message) {
        super(message);
    }
}
