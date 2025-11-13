package com.example.ClarifyAi.service;

import com.example.ClarifyAi.exception.UnknownActionException;
import com.example.ClarifyAi.mapper.ActionMapper;
import com.example.ClarifyAi.model.ActionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.ClarifyAi.utilityClass.Utility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PromptServiceTest {

    @Autowired
    private PromptService promptService;

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
    void shouldGenerateSummaryPromptWithCustomMaxWords() {
        var prompt = promptService.getPrompt(MAX_WORDS_REQUEST);
        String content = ((SystemMessage) prompt.getInstructions().getFirst()).getText();

        assertTrue(content.trim().startsWith("Riassumi il seguente testo in modo chiaro:"));
        assertTrue(content.trim().endsWith("Usa massimo 100 parole."));
    }

    @Test
    void shouldGenerateSimplifyPromptWithFallbackLength() {
        var prompt = promptService.getPrompt(LENGTH_REQUEST);
        String content = ((SystemMessage) prompt.getInstructions().getFirst()).getText();

        assertTrue(content.trim().startsWith("Spiega in modo semplice e comprensibile:"));
        assertTrue(content.trim().endsWith("Usa massimo 60 parole."));
    }

    @Test
    void shouldGenerateTranslateItPrompt() {
        var prompt = promptService.getPrompt(VALID_TRANSLATE_IT_REQUEST);
        String content = ((SystemMessage) prompt.getInstructions().getFirst()).getText();

        assertTrue(content.trim().startsWith("Traduci in italiano il seguente testo:"));
    }

    @Test
    void shouldGenerateTranslateEnPrompt() {
        var prompt = promptService.getPrompt(VALID_TRANSLATE_EN_REQUEST);
        String content = ((SystemMessage) prompt.getInstructions().getFirst()).getText();

        assertTrue(content.trim().startsWith("Translate the following text into English:"));
    }

    @Test
    void shouldGeneratePersonalizedActionPrompt() {
        var prompt = promptService.getPrompt(VALID_PERSONALIZED_ACTION_REQUEST);
        String content = ((SystemMessage) prompt.getInstructions().getFirst()).getText();

        assertTrue(content.trim().startsWith("Traducilo in francese."));
    }

    @Test
    void shouldUseDefaultPromptForUnknownAction() {
        UnknownActionException ex = assertThrows(UnknownActionException.class, () -> promptService.getPrompt(NULL_ACTION_REQUEST));

        assertTrue(ex.getMessage().contains("Action cannot be null or empty."));
    }
}