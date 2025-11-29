package com.example.ClarifyAi.session;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class SessionStore {

    private final Map<String, SessionData> sessions = new HashMap<>();

    public String createSession(String context) {
        String id = UUID.randomUUID().toString();
        sessions.put(id, new SessionData(context, new ArrayList<>(), System.currentTimeMillis()));
        return id;
    }

    public SessionData getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}
