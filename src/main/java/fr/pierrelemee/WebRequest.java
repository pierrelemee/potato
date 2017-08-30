package fr.pierrelemee;


import com.sun.net.httpserver.HttpExchange;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebRequest {

    protected HttpMethod method;
    protected String path;
    protected Map<String, String> variables;
    protected Map<String, String> cookies;
    protected Map<String, List<String>> headers;
    protected Map<String, List<String>> get;
    protected Map<String, List<String>> post;

    public WebRequest(String path) {
        this(path, HttpMethod.GET);
    }

    public WebRequest(String path, HttpMethod method) {
        this(path, method, Collections.emptyMap(), Collections.emptyMap());
    }

    public WebRequest(String path, HttpMethod method, Map<String, List<String>> headers, Map<String, List<String>> get) {
        this.path = path;
        this.method = method;
        this.get = get;
        this.variables = new LinkedHashMap<>();
        this.cookies = new LinkedHashMap<>();
        this.headers = headers;

        if (this.headers.containsKey("Cookie")) {
            for (String header: this.headers.get("Cookie")) {
                for (String cookie: header.split(";")) {
                    cookie = cookie.trim();
                    int index = cookie.indexOf('=');
                    if (index >= 0) {
                        this.cookies.put(cookie.substring(0, index), cookie.substring(index + 1));
                    }
                }
            }
        }
    }

    public static Map<String, List<String>> parameters(String query) {
        Map<String, List<String>> parameters = new LinkedHashMap<>();

        if (query != null && !query.isEmpty()) {
            int index;
            for (String parameter: query.split("&")) {
                index = parameter.indexOf('=');
                if (index > -1) {
                    parameters.put(parameter.substring(0, index), Collections.singletonList(parameter.substring(index + 1)));
                } else {
                    parameters.put(parameter, Collections.singletonList("true"));
                }
            }
        }

        return parameters;
    }

    protected void addVariables(Map<String, String> variables) {
        this.variables.putAll(variables);
    }

    public String variable(String name) {
        return this.variables.getOrDefault(name, null);
    }

    public boolean hasCookie(String name) {
        return this.cookies.containsKey(name);
    }

    public String cookie(String name) {
        return this.cookies.getOrDefault(name, null);
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, List<String>> get() {
        return this.get;
    }

    public Map<String, List<String>> post() {
        return this.post;
    }

    public static WebRequest fromExchange(HttpExchange exchange) {
        return new WebRequest(exchange.getRequestURI().getPath(),
                HttpMethod.valueOf(exchange.getRequestMethod()),
                exchange.getRequestHeaders(),
                parameters(exchange.getRequestURI().getQuery())
                );
    }
}
