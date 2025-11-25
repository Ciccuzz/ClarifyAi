package com.example.ClarifyAi.service;

import com.example.ClarifyAi.dto.PromptRequest;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.ClarifyAi.utilityClass.Utility.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PromptServiceTest {

    @Autowired
    private PromptService promptService;

    @Test
    void getPrompt_shouldReturnPromptWithUserMessage() {
        // when
        Prompt result = promptService.getPrompt(VALID_REQUEST2);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserMessage()).isNotNull();
        assertThat(result.getUserMessage().getText()).isEqualTo("Spiegami in poche parole cosa è un cavallo");
    }



}