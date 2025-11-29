package com.example.ClarifyAi.utilityClass;

import com.example.ClarifyAi.dto.ChatRequest;
import com.example.ClarifyAi.dto.StartSessionRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utility {

    public static final String TOO_LONG_VALID_TEXT = "Word ".repeat(1100);
    public static final String VALID_TEXT = "Spiegami in poche parole cosa è un cavallo";
    public static final String EMPTY_TEXT = "";
    public static final String CONTEXT = "context";


    public static final ChatRequest VALID_REQUEST = new ChatRequest("1", VALID_TEXT);
    public static final ChatRequest NULL_TEXT_REQUEST = new ChatRequest("1", null);
    public static final ChatRequest TOO_LONG_TEXT_REQUEST = new ChatRequest("1", TOO_LONG_VALID_TEXT);
    public static final ChatRequest NULL_SESSION_ID_REQUEST = new ChatRequest(null, VALID_TEXT);
    public static final ChatRequest EMPTY_TEXT_REQUEST = new ChatRequest("1", EMPTY_TEXT);


    public static final StartSessionRequest VALID_START_SESSION = new StartSessionRequest(CONTEXT);
    public static final StartSessionRequest NULL_CONTEXT_START_SESSION = new StartSessionRequest(null);

}