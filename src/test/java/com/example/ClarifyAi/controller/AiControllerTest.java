package com.example.ClarifyAi.controller;

import com.example.ClarifyAi.dto.ChatRequest;
import com.example.ClarifyAi.dto.ChatResponse;
import com.example.ClarifyAi.service.PromptService;
import com.example.ClarifyAi.session.SessionData;
import com.example.ClarifyAi.session.SessionStore;
import com.example.ClarifyAi.utility.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AiControllerTest {

    @Mock
    private PromptService promptService;

    @Mock
    private OpenAiChatModel chatModel;

    @Mock
    private Validator validator;

    @Mock
    private SessionStore sessionStore;

    @InjectMocks
    private AiController aiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void chat_shouldReturnAiResponse_andUpdateSession() {
        // ARRANGE
        ChatRequest request = new ChatRequest("SESSION1", "Ciao AI!");

        SessionData session = new SessionData(
                "Contesto",
                new ArrayList<>(),
                System.currentTimeMillis()
        );
        when(sessionStore.getSession("SESSION1")).thenReturn(session);

        Prompt prompt = mock(Prompt.class);
        when(promptService.getPrompt(session, "Ciao AI!")).thenReturn(prompt);

        // ChatResponse di Spring AI
        org.springframework.ai.chat.model.ChatResponse springResponse =
                mock(org.springframework.ai.chat.model.ChatResponse.class);

        Generation generation = mock(Generation.class);
        AssistantMessage aiMessage = mock(AssistantMessage.class);

        when(chatModel.call(prompt)).thenReturn(springResponse);
        when(springResponse.getResult()).thenReturn(generation);

        when(generation.getOutput()).thenReturn(aiMessage);

        when(aiMessage.getText()).thenReturn("Risposta AI");

        // ACT
        ChatResponse response = aiController.chat(request);

        // ASSERT
        assertEquals("Risposta AI", response.result());
        assertEquals(2, session.getMessages().size());
        assertEquals("Utente: Ciao AI!", session.getMessages().get(0));
        assertEquals("AI: Risposta AI", session.getMessages().get(1));

        verify(validator).checkChatRequest(request);
        verify(promptService).getPrompt(session, "Ciao AI!");
        verify(validator).checkChatResponse("Risposta AI");
    }

    @Test
    void chat_shouldThrowException_whenSessionNotFound() {
        // ARRANGE
        ChatRequest request = new ChatRequest("SESSION_MISSING", "Ciao");

        // Simula sessione non trovata
        when(sessionStore.getSession("SESSION_MISSING")).thenReturn(null);

        // ACT + ASSERT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> aiController.chat(request)
        );

        assertEquals("Sessione non trovata: SESSION_MISSING", ex.getMessage());

        // Nessuna interazione con promptService, chatModel, validator
        verifyNoInteractions(promptService);
        verifyNoInteractions(chatModel);
    }

}
