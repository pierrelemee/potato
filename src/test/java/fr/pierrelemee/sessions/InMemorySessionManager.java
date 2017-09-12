package fr.pierrelemee.sessions;

import fr.pierrelemee.SessionManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class InMemorySessionManager extends SessionManager<SimpleSession> {

    protected Map<String, SimpleSession> sessions;

    public InMemorySessionManager() {
        super(SimpleSession::new);
        this.sessions = new LinkedHashMap<>();
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
    public void flush(String hash, SimpleSession session) {
        this.sessions.put(hash, session);
    }

    @Override
    public void destroySession(SimpleSession session) {
        this.sessions.remove(session);
    }
}
