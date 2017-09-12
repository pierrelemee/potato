package fr.pierrelemee.sessions;

import fr.pierrelemee.Session;

public class SimpleSession implements Session {

    protected int count = 0;

    public int increment() {
        return ++this.count;
    }
}
