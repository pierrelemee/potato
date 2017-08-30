package fr.pierrelemee;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MockClient {

    protected WebApplication app;
    protected Map<String, Cookie> cookies;

    public MockClient(WebApplication app) {
        this.app = app;
        this.cookies = new LinkedHashMap<>();
    }

    public WebResponse get(String path) {
        return this.request(path, HttpMethod.GET);
    }

    public WebResponse post(String path) {
        return this.request(path, HttpMethod.POST);
    }

    protected WebResponse request(String path, HttpMethod method) {
        WebRequest request = new WebRequest(path, method);

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

        WebResponse response = this.app.process(request);

        this.cookies.putAll(response.getCookies());
        return response;
    }
}
