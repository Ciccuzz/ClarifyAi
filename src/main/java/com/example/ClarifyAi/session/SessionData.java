package com.example.ClarifyAi.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SessionData {

    private final String context;
    private final List<String> messages;
    private long lastAccess;

    public void updateAccessTime() {
        this.lastAccess = System.currentTimeMillis();
    }
}
