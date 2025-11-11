package com.example.ClarifyAi.controller;

import com.example.ClarifyAi.dto.PromptRequest;
import com.example.ClarifyAi.exception.NotValidRequestException;
import com.example.ClarifyAi.exception.NullResponseException;
import com.example.ClarifyAi.model.Length;
import com.example.ClarifyAi.service.AiService;
import com.example.ClarifyAi.service.ValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.ClarifyAi.utility.Utility.NULL_TEXT_REQUEST;
import static com.example.ClarifyAi.utility.Utility.VALID_SUMMARY_REQUEST;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AiController.class)
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AiService aiService;

    @MockitoBean
    private ValidationService validationService;

    @MockitoBean
    private OpenAiChatModel chatModel;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void handlePrompt_shouldReturnValidResponse_whenRequestIsCorrect() throws Exception {
        PromptRequest promptRequest = VALID_SUMMARY_REQUEST;
        Prompt mockedPrompt = new Prompt(List.of());
        String expectedText = "Risposta mockata";

        AssistantMessage assistantMessage = new AssistantMessage("Risposta mockata");
        Generation generation = new Generation(assistantMessage);
        ChatResponse mockChatResponse = new ChatResponse(List.of(generation));

        when(aiService.getPrompt(any(PromptRequest.class))).thenReturn(mockedPrompt);
        when(chatModel.call(any(Prompt.class))).thenReturn(mockChatResponse);

        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promptRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(expectedText));

        verify(validationService).checkRequest(promptRequest);
        verify(validationService).checkResponse(expectedText);
    }

    @Test
    void handlePrompt_shouldReturnBadRequest_whenRequestIsInvalid() throws Exception {
        PromptRequest invalidRequest = NULL_TEXT_REQUEST;

        doThrow(new NotValidRequestException("Text cannot be null."))
                .when(validationService).checkRequest(invalidRequest);

        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Text cannot be null."));

        verify(validationService).checkRequest(invalidRequest);
        verifyNoInteractions(aiService, chatModel);
    }

    @Test
    void handlePrompt_shouldReturnBadRequest_whenResponseIsNull() throws Exception {
        Prompt mockedPrompt = new Prompt(List.of());

        AssistantMessage assistantMessage = new AssistantMessage(null);
        Generation generation = new Generation(assistantMessage);
        ChatResponse mockChatResponse = new ChatResponse(List.of(generation));

        when(aiService.getPrompt(any(PromptRequest.class))).thenReturn(mockedPrompt);
        when(chatModel.call(any(Prompt.class))).thenReturn(mockChatResponse);

        doThrow(new NullResponseException("The response is null."))
                .when(validationService).checkResponse(null);

        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_SUMMARY_REQUEST)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("NULL_RESPONSE"))
                .andExpect(jsonPath("$.message").value("The response is null."));
    }


//    @Test
//    void handlePrompt_shouldReturnAiResponse() throws Exception {
//
//        Prompt mockedPrompt = new Prompt(List.of());
//
//        AssistantMessage assistantMessage = new AssistantMessage("Risposta mockata");
//        Generation generation = new Generation(assistantMessage);
//        ChatResponse mockChatResponse = new ChatResponse(List.of(generation));
//
//        when(aiService.getPrompt(any(PromptRequest.class))).thenReturn(mockedPrompt);
//        when(chatModel.call(any(Prompt.class))).thenReturn(mockChatResponse);
//
//        // Act & Assert
//        mockMvc.perform(post("/api").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(VALID_SUMMARY_REQUEST))).andExpect(status().isOk()).andExpect(jsonPath("$.result").value("Risposta mockata"));
//    }

//    @Test
//    void handlePrompt_shouldReturnBadRequest_whenResponseIsNull() throws Exception {
//
//        Prompt prompt = new Prompt(List.of());
//
//        AssistantMessage assistantMessage = new AssistantMessage(null); // Simula testo null
//        Generation generation = new Generation(assistantMessage);
//        ChatResponse chatResponse = new ChatResponse(List.of(generation));
//
//        when(aiService.getPrompt(any(PromptRequest.class))).thenReturn(prompt);
//        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);
//
//        doThrow(new NullResponseException("The response is null"))
//                .when(aiService)
//                .checkResponse(null);
//
//        mockMvc.perform(post("/api")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(VALID_SUMMARY_REQUEST)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("NULL_RESPONSE"))
//                .andExpect(jsonPath("$.message").value("The response is null"))
//                .andExpect(jsonPath("$.timestamp").exists());
//    }
}