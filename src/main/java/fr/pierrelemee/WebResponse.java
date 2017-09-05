package fr.pierrelemee;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebResponse {

    protected final int status;
    protected Body body;
    protected Map<String, Cookie> cookies;
    protected Map<String, List<String>> headers;

    private WebResponse() {
        this(200, "");
    }

    public WebResponse(String body) {
        this(200, body);
    }

    private WebResponse(int status) {
        this(status, "");
    }

    public WebResponse(int status, String body) {
        this.status = status;
        this.cookies = new LinkedHashMap<>();
        this.headers = new LinkedHashMap<>();
        this.setBody(body);
    }

    protected WebResponse setBody(Body body) {
        this.body = body;

        return this;
    }

    public WebResponse setBody(String content) {
        return this.setBody(RawBody.fromContent(content));
    }

    public WebResponse setTemplate(String path) {
        return this.setBody(TemplateBody.fromPath(path));
    }

    public WebResponse setTemplate(String path, Map<String, Object> parameters) {
        return this.setBody(TemplateBody.fromPath(path, parameters));
    }

    public WebResponse setTemplate(TemplateBody template) {
        return this.setBody(template);
    }

    public Body getBody() {
        return this.body;
    }

    public WebResponse addHeader(String name, String value) {
        if (!this.headers.containsKey(name)) {
            this.headers.put(name, Collections.singletonList(value));
        } else {
            this.headers.get(name).add(value);
        }

        return this;
    }

    public WebResponse addCookie(Cookie cookie) {
        this.cookies.put(cookie.getKey(), cookie);
        return this.addHeader("Set-Cookie", cookie.getHeader());
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, Cookie> getCookies() {
        return cookies;
    }

    public static WebResponse ok() {
        return new WebResponse();
    }

    public static WebResponse status(int status) {
        return new WebResponse(status);
    }

    public static WebResponse redirect(String location) {
        return redirect(location, false);
    }

    public static WebResponse redirect(String location, boolean permanent) {
        return WebResponse
            .status(permanent ? 301 : 302)
            .addHeader("Location", location);
    }
}
