package com.example.ClarifyAi.dto;


import com.example.ClarifyAi.model.Length;

public record PromptRequest (String text, String action, String personalizedAction, Length length, Integer maxWords ){}
