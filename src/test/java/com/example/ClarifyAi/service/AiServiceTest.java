package com.example.ClarifyAi.service;

import com.example.ClarifyAi.exception.NotValidTextException;
import com.example.ClarifyAi.exception.NullResponseException;
import com.example.ClarifyAi.exception.UnknownActionException;
import com.example.ClarifyAi.mapper.ActionMapper;
import com.example.ClarifyAi.model.ActionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.ClarifyAi.utility.Utility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AiServiceTest {

    @Autowired
    private AiService aiService;

    @Mock
    private ActionMapper actionMapper;


    @BeforeEach
    void setup() {
        when(actionMapper.toEnum("SUMMARY")).thenReturn(ActionEnum.SUMMARY);
        when(actionMapper.toEnum("SIMPLIFY")).thenReturn(ActionEnum.SIMPLIFY);
        when(actionMapper.toEnum("TRANSLATE_IT")).thenReturn(ActionEnum.TRANSLATE_IT);
        when(actionMapper.toEnum("TRANSLATE_EN")).thenReturn(ActionEnum.TRANSLATE_EN);
        when(actionMapper.toEnum(any())).thenThrow(new UnknownActionException("Action cannot be null or empty."));
    }


    @Test
    void getPromptShouldThrowExceptionWhenTextTooLong() {
        assertThrows(NotValidTextException.class, () -> aiService.getPrompt(TOO_LONG_REQUEST));
    }

    @Test
    void shouldReturnPromptWhenTextIsValid() {
        var prompt = aiService.getPrompt(VALID_SUMMARY_REQUEST);
        assertNotNull(prompt);
        assertFalse(prompt.getInstructions().isEmpty());
    }

    @Test
    void shouldGenerateSummaryPromptWithCustomMaxWords() {
        var prompt = aiService.getPrompt(MAX_WORDS_REQUEST);
        String content = ((SystemMessage) prompt.getInstructions().getFirst()).getText();

        assertTrue(content.trim().startsWith("Riassumi il seguente testo in modo chiaro:"));
        assertTrue(content.trim().endsWith("Usa massimo 100 parole."));
    }

    @Test
    void shouldGenerateSimplifyPromptWithFallbackLength() {
        var prompt = aiService.getPrompt(LENGTH_REQUEST);
        String content = ((SystemMessage) prompt.getInstructions().getFirst()).getText();

        assertTrue(content.trim().startsWith("Spiega in modo semplice e comprensibile:"));
        assertTrue(content.trim().endsWith("Usa massimo 60 parole."));
    }

    @Test
    void shouldGenerateTranslateItPrompt() {
        var prompt = aiService.getPrompt(VALID_TRANSLATE_IT_REQUEST);
        String content = ((SystemMessage) prompt.getInstructions().getFirst()).getText();

        assertTrue(content.trim().startsWith("Traduci in italiano il seguente testo:"));
    }

    @Test
    void shouldGenerateTranslateEnPrompt() {
        var prompt = aiService.getPrompt(VALID_TRANSLATE_EN_REQUEST);
        String content = ((SystemMessage) prompt.getInstructions().getFirst()).getText();

        assertTrue(content.trim().startsWith("Translate the following text into English:"));
    }

    @Test
    void shouldUseDefaultPromptForUnknownAction() {
        UnknownActionException ex = assertThrows(UnknownActionException.class, () -> aiService.getPrompt(NULL_ACTION_REQUEST));

        assertTrue(ex.getMessage().contains("Action cannot be null or empty."));
    }

    @Test
    void shouldThrowErrorCauseOfEmptyText() {
        NotValidTextException ex = assertThrows(NotValidTextException.class, () -> aiService.getPrompt(EMPTY_TEXT_REQUEST));

        assertTrue(ex.getMessage().contains("Text cannot be null or empty."));
    }

    @Test
    void shouldThrowErrorCauseOfNullText() {
        NotValidTextException ex = assertThrows(NotValidTextException.class, () -> aiService.getPrompt(NULL_TEXT_REQUEST));

        assertTrue(ex.getMessage().contains("Text cannot be null or empty."));
    }

    @Test
    void shouldThrowExceptionCauseOfNullResponse() {
        NullResponseException ex = assertThrows(NullResponseException.class, () -> aiService.checkResponse(null));

        assertTrue(ex.getMessage().contains("The response is null."));
    }

    @Test
    void shouldNotThrowExceptionWhenResponseIsNotNull() {
        assertDoesNotThrow(() -> aiService.checkResponse("Risposta valida"));
    }
}