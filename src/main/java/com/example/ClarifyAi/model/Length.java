package com.example.ClarifyAi.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Length {
    //VERY_SHORT(30),
    SHORT(60),
    MEDIUM(100),
    LONG(150),
    //EXTRA_LONG(200),
    TRANSLATION(1000),
    PERSONALIZED(0);

    public final int words;
}
