package fr.pierrelemee;

public interface RequestProcess {

    public WebResponse process(WebRequest request, Session session) throws Exception;
}
