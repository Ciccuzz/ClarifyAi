package com.example.ClarifyAi.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ActionEnum {
    SUMMARY("Riassumi il seguente testo in modo chiaro:"),
    SIMPLIFY("Spiega in modo semplice e comprensibile:"),
    TRANSLATE_IT("Traduci in italiano il seguente testo:"),
    TRANSLATE_EN("Translate the following text into English:");

    public final String string;
}