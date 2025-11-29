package com.example.ClarifyAi.exception;

public class NotValidStartSessionRequest extends RuntimeException {
    public NotValidStartSessionRequest(String message) {
        super(message);
    }
}

