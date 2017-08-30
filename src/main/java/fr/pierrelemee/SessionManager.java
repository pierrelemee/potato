package fr.pierrelemee;


public abstract class SessionManager<T extends Session> {

    protected SessionFactory<T> factory;

    public SessionManager(SessionFactory<T> factory) {
        this.factory = factory;
    }

    public T extract(WebRequest request) {
        if (request.hasCookie(this.getSessionCookieName()) && this.hasSession(request.cookie(this.getSessionCookieName()))) {
            return this.getSession(request.cookie(this.getSessionCookieName()));
        }

        return null;
    }
    public abstract String getSessionCookieName();

    public abstract boolean hasSession(String hash);

    public abstract T getSession(String hash);

    protected abstract void addSession(T session);

    public T createSession() {
        T session = this.factory.createSession();
        while (this.hasSession(session.getHash())) {
            session = this.factory.createSession();
        }

        this.addSession(session);

        return session;
    }

    public void destroySession(T session) {}
}
