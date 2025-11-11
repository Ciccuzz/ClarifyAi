package com.example.ClarifyAi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotValidRequestException.class)
    public ResponseEntity<Map<String, Object>> handleTooLongText(NotValidRequestException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "INVALID_REQUEST");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(UnknownActionException.class)
    public ResponseEntity<Map<String, Object>> handleTooLongText(UnknownActionException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "UNKNOWN_ACTION");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NullResponseException.class)
    public ResponseEntity<Map<String, Object>> handleNullResponse(NullResponseException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "NULL_RESPONSE");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "INTERNAL_ERROR");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
