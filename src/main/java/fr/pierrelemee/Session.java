package fr.pierrelemee;

import java.util.Random;

public abstract class Session {

    private static final Random RANDOM = new Random();

    protected final String hash;
    protected boolean sent = false;

    public Session() {
        this(generateHash());
    }

    public Session(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public boolean isSent() {
        return sent;
    }

    public void send() {
        this.sent = true;
    }

    private static String generateHash() {
        return Long.toHexString(RANDOM.nextLong());
    }
}
