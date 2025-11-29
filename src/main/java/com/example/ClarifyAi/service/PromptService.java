package com.example.ClarifyAi.service;

import com.example.ClarifyAi.session.SessionData;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptService {

    public Prompt getPrompt(SessionData session, String userMessage) {

        String system = """
                Sei una AI che risponde alle domande basandosi sul contesto della pagina
                e sulla conversazione precedente. Se l'utente chiede qualcosa fuori contesto,
                deduci la risposta ma indica che non hai informazioni a riguardo.
                """;

        String combinedUserPrompt = """
                CONTEXT:
                %s

                HISTORY:
                %s

                USER MESSAGE:
                %s
                """.formatted(
                session.getContext(),
                String.join("\n", session.getMessages()),
                userMessage
        );

        return new Prompt(
                new org.springframework.ai.chat.messages.SystemMessage(system),
                new UserMessage(combinedUserPrompt)
        );
    }
}
