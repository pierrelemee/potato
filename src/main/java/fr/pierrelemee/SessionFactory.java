package fr.pierrelemee;

public interface SessionFactory<T extends Session> {

    public T createSession();
}
