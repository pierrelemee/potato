package fr.pierrelemee;

import java.io.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MockClient {

    protected WebApplication app;
    protected Map<String, Cookie> cookies;
    protected String body;

    public MockClient(WebApplication app) {
        this.app = app;
        this.cookies = new LinkedHashMap<>();
    }

    public String getBody() {
        return body;
    }

    public WebResponse get(String path) {
        return this.request(path, HttpMethod.GET);
    }

    public WebResponse post(String path) {
        return this.post(path, Collections.emptyMap());
    }

    public WebResponse post(String path, Map<String, String> parameters) {
        return this.request(path, HttpMethod.POST, parameters);
    }

    protected WebResponse request(String path, HttpMethod method) {
        return this.request(path, method, Collections.emptyMap());
    }

    protected WebResponse request(String path, HttpMethod method, Map<String, String> parameters) {
        WebRequest request = new WebRequest(path, method, new Parameters(parameters.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Collections.singletonList(e.getValue())))));

        for (String key: this.cookies.keySet()) {
            if (this.cookies.get(key).hasExpired()) {
                this.cookies.remove(key);
            }
        }

        if (!this.cookies.isEmpty()) {
            request.addHeader("Cookie",
                String.join(", ",
                    this.cookies.values().stream()
                        .map(c -> c.getKey() + "=" + c.getValue())
                        .collect(Collectors.toList())
                )
            );
        }

        this.body = "";
        WebResponse response = this.app.process(request);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            if (response.getBody() != null) {
                response.getBody().write(buffer, this.app.getRenderer());
            }
            buffer.flush();
            buffer.close();
            this.body = buffer.toString();
        } catch (IOException ioe) {}

        this.cookies.putAll(response.getCookies());
        return response;
    }
}
