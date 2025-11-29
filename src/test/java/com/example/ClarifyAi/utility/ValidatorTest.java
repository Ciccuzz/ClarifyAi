package com.example.ClarifyAi.utility;

import com.example.ClarifyAi.exception.NotValidChatRequestException;
import com.example.ClarifyAi.exception.NotValidStartSessionRequest;
import com.example.ClarifyAi.exception.NullResponseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.ClarifyAi.utilityClass.Utility.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidatorTest {

    @Autowired
    private Validator validator;


    // CHAT REQUEST VALIDATOR

    //when request is null
    @Test
    void shouldThrowErrorCauseOfNullRequest() {
        NotValidChatRequestException ex = assertThrows(NotValidChatRequestException.class, () -> validator.checkChatRequest(null));

        assertTrue(ex.getMessage().contains("Request body is null."));
    }

    //when text is null
    @Test
    void shouldThrowErrorCauseOfNullText() {
        NotValidChatRequestException ex = assertThrows(NotValidChatRequestException.class, () -> validator.checkChatRequest(NULL_TEXT_REQUEST));

        assertTrue(ex.getMessage().contains("Text cannot be null."));
    }

    @Test
    void shouldThrowErrorCauseOfNullSessionId() {
        NotValidChatRequestException ex = assertThrows(NotValidChatRequestException.class, () -> validator.checkChatRequest(NULL_SESSION_ID_REQUEST));

        assertTrue(ex.getMessage().contains("Session Id cannot be null."));
    }

    //when text is empty
    @Test
    void shouldThrowErrorCauseOfEmptyText() {
        NotValidChatRequestException ex = assertThrows(NotValidChatRequestException.class, () -> validator.checkChatRequest(EMPTY_TEXT_REQUEST));

        assertTrue(ex.getMessage().contains("Text cannot be empty."));
    }

    @Test
    void shouldNotThrowErrorCauseTextIsNotEmpty() {
        assertDoesNotThrow(() -> validator.checkChatRequest(VALID_REQUEST));
    }

    @Test
    void shouldThrowErrorCauseOfTooLongText() {
        NotValidChatRequestException ex = assertThrows(NotValidChatRequestException.class, () -> validator.checkChatRequest(TOO_LONG_TEXT_REQUEST));

        assertTrue(ex.getMessage().contains("The entered text is too long (1100 words). Use max 1000 words."));
    }

    //when Response is null
    @Test
    void shouldThrowExceptionCauseOfNullResponse() {
        NullResponseException ex = assertThrows(NullResponseException.class, () -> validator.checkChatResponse(null));

        assertTrue(ex.getMessage().contains("The response is null."));
    }

    @Test
    void shouldNotThrowExceptionWhenResponseIsNotNull() {
        assertDoesNotThrow(() -> validator.checkChatResponse("Valid Response"));
    }


    // SESSION VALIDATOR

    // when StartSessionRequest is null
    @Test
    void shouldThrowErrorCauseOfNullStartSessionRequest() {
        NotValidStartSessionRequest ex = assertThrows(NotValidStartSessionRequest.class, () -> validator.checkStartSessionRequest(null));

        assertTrue(ex.getMessage().contains("Start Session Request is null."));
    }

    //when context is null
    @Test
    void shouldThrowErrorCauseOfNullContext() {
        NotValidStartSessionRequest ex = assertThrows(NotValidStartSessionRequest.class, () -> validator.checkStartSessionRequest(NULL_CONTEXT_START_SESSION));

        assertTrue(ex.getMessage().contains("Context cannot be null."));
    }
}