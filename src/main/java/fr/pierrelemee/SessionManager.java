package fr.pierrelemee;

import java.util.Random;

public abstract class SessionManager<T extends Session> {

    private static final String COOKIE_NAME = "pttossnid";
    private static final Random RANDOM = new Random();
    protected SessionFactory<T> factory;

    public SessionManager(SessionFactory<T> factory) {
        this.factory = factory;
    }

    public final String getCookieName() {
        return COOKIE_NAME;
    }

    public abstract boolean hasSession(String hash);

    public abstract T getSession(String hash);

    protected T createSession() {
        return this.factory.createSession();
    }

    public abstract void flush(String hash, T session);

    public abstract void destroySession(String hash, T session);

    public String generateHash() {
        String hash = Long.toHexString(RANDOM.nextLong());
        while (this.hasSession(hash)) {
            hash = Long.toHexString(RANDOM.nextLong());
        }

        return hash;
    }
}
