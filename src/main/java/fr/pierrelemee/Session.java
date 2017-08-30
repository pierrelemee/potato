package fr.pierrelemee;

import java.util.Random;

public abstract class Session {

    private static final Random RANDOM = new Random();

    protected final String hash;

    public Session() {
        this(generateHash());
    }

    public Session(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    private static String generateHash() {
        return Long.toHexString(RANDOM.nextLong());
    }
}
