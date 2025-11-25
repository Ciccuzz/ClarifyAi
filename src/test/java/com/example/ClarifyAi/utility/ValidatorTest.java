package com.example.ClarifyAi.utility;

import com.example.ClarifyAi.exception.NotValidRequestException;
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

    //when request is null
    @Test
    void shouldThrowErrorCauseOfNullRequest() {
        NotValidRequestException ex = assertThrows(NotValidRequestException.class, () -> validator.checkRequest(null));

        assertTrue(ex.getMessage().contains("Request body is null."));
    }

    //when text is null
    @Test
    void shouldThrowErrorCauseOfNullText() {
        NotValidRequestException ex = assertThrows(NotValidRequestException.class, () -> validator.checkRequest(NULL_TEXT_REQUEST));

        assertTrue(ex.getMessage().contains("Text cannot be null."));
    }

    //when text is empty
    @Test
    void shouldThrowErrorCauseOfEmptyText() {
        NotValidRequestException ex = assertThrows(NotValidRequestException.class, () -> validator.checkRequest(EMPTY_TEXT_REQUEST));

        assertTrue(ex.getMessage().contains("Text cannot be empty."));
    }

    @Test
    void shouldNotThrowErrorCauseTextIsNotEmpty() {
        assertDoesNotThrow(() -> validator.checkRequest(VALID_REQUEST));
    }

    //when text too long
    @Test
    void shouldThrowErrorCauseOfTooLongText() {
        NotValidRequestException ex = assertThrows(NotValidRequestException.class, () -> validator.checkRequest(TOO_LONG_REQUEST));

        assertTrue(ex.getMessage().contains("The entered text is too long (1100 words). Use max 1000 words."));
    }

    //when Response is null
    @Test
    void shouldThrowExceptionCauseOfNullResponse() {
        NullResponseException ex = assertThrows(NullResponseException.class, () -> validator.checkResponse(null));

        assertTrue(ex.getMessage().contains("The response is null."));
    }

    @Test
    void shouldNotThrowExceptionWhenResponseIsNotNull() {
        assertDoesNotThrow(() -> validator.checkResponse("Valid Response"));
    }

}