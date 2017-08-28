package fr.pierrelemee;

public class WebResponse {

    protected int status;
    protected String body;

    public WebResponse(String body) {
        this(200, body);
    }

    public WebResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
