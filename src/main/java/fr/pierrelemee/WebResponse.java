package fr.pierrelemee;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebResponse {

    protected final int status;
    protected StringBuffer body;
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
        this.body = new StringBuffer();
        this.body.append(body);
        this.headers = new LinkedHashMap<>();
    }

    public WebResponse writeBody(String body) {
        this.body.append(body);

        return this;
    }

    public WebResponse addHeader(String name, String value) {
        if (!this.headers.containsKey(name)) {
            this.headers.put(name, Collections.singletonList(value));
        } else {
            this.headers.get(name).add(value);
        }

        return this;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() throws IOException {
        return this.body.toString();
    }

    public static WebResponse ok() {
        return new WebResponse();
    }

    public static WebResponse status(int status) {
        return new WebResponse(status);
    }
}
