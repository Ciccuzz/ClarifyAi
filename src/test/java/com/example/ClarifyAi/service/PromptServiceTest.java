package com.example.ClarifyAi.service;

import com.example.ClarifyAi.session.SessionData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.ClarifyAi.utilityClass.Utility.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PromptServiceTest {

    @Autowired
    private PromptService promptService;

    @Test
    void shouldBuildPromptCorrectly() {
        // Arrange
        SessionData session = new SessionData(
                "Contesto di esempio",
                List.of("Messaggio 1", "Messaggio 2"),
                System.currentTimeMillis()
        );

        String userMessage = "Domanda dell'utente";

        // Act
        Prompt prompt = promptService.getPrompt(session, userMessage);

        // Assert
        assertNotNull(prompt);

        List<Message> messages = prompt.getInstructions();
        assertEquals(2, messages.size());

        // --- SYSTEM MESSAGE ---
        Message systemMsg = messages.getFirst();
        assertInstanceOf(SystemMessage.class, systemMsg);
        String systemText = systemMsg.getText();

        assertTrue(systemText.contains("Sei una AI"));
        assertTrue(systemText.contains("fuori contesto"));

        // --- USER MESSAGE ---
        Message userMsg = messages.get(1);
        assertInstanceOf(UserMessage.class, userMsg);
        String userText = userMsg.getText();

        assertTrue(userText.contains("CONTEXT:"));
        assertTrue(userText.contains("Contesto di esempio"));

        assertTrue(userText.contains("HISTORY:"));
        assertTrue(userText.contains("Messaggio 1"));
        assertTrue(userText.contains("Messaggio 2"));

        assertTrue(userText.contains("USER MESSAGE:"));
        assertTrue(userText.contains("Domanda dell'utente"));
    }
}
