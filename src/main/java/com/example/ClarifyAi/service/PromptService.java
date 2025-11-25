package com.example.ClarifyAi.service;

import com.example.ClarifyAi.dto.PromptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptService {

    public Prompt getPrompt(PromptRequest promptRequest) {

        String finalPrompt = """
        L'utente ha scritto: %s

        ----------
        Contesto della pagina (testo estratto dalla pagina web):
        %s
        ----------

        Usa il contesto sopra per rispondere in modo accurato.
    """.formatted(
                promptRequest.text(),
                promptRequest.context() != null ? promptRequest.context() : "(nessun contesto disponibile)"
        );

        return new Prompt(new UserMessage(finalPrompt));
    }

}
