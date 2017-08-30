package fr.pierrelemee.sessions;

import fr.pierrelemee.SessionManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class InMemorySessionManager extends SessionManager<SimpleSession> {

    public static final String COOKIE_NAME = "ptossnid";

    protected Map<String, SimpleSession> sessions;

    public InMemorySessionManager() {
        super(SimpleSession::new);
        this.sessions = new LinkedHashMap<>();
    }

    @Override
    public String getSessionCookieName() {
        return COOKIE_NAME;
    }

    @Override
    public boolean hasSession(String hash) {
        return this.sessions.containsKey(hash);
    }

    @Override
    public SimpleSession getSession(String hash) {
        return this.sessions.get(hash);
    }

    @Override
    protected void addSession(SimpleSession session) {
        this.sessions.put(session.getHash(), session);
    }
}
