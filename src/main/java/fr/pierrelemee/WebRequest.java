package fr.pierrelemee;


import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebRequest {

    private static final String COOKIE_HEADER_NAME = "Cookie";

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
        this(path, method, new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    public WebRequest(String path, HttpMethod method, Map<String, List<String>> headers, Map<String, List<String>> get) {
        this(path, method, headers, get, Collections.emptyMap());
    }

    public WebRequest(String path, HttpMethod method, Map<String, List<String>> headers, Map<String, List<String>> get, Map<String, List<String>> post) {
        this.path = path;
        this.method = method;
        this.get = get;
        this.post = post;
        this.variables = new LinkedHashMap<>();
        this.cookies = new LinkedHashMap<>();
        this.headers = headers;

        this.exctractCookies();
    }

    public static Map<String, List<String>> parameters(InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder buffer = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            return parameters(buffer.toString());

        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }

    public static Map<String, List<String>> parameters(String query) {
        Map<String, List<String>> parameters = new LinkedHashMap<>();

        if (query != null && !query.isEmpty()) {
            int index;
            for (String parameter : query.split("&")) {
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

    protected void addHeader(String name, String value) {
        if (this.headers.containsKey(name)) {
            this.headers.get(name).add(value);
        } else {
            this.headers.put(name, Collections.singletonList(value));
        }

        if (name.trim().equalsIgnoreCase(COOKIE_HEADER_NAME)) {
            this.exctractCookies();
        }
    }

    protected void exctractCookies() {
        if (this.headers.containsKey(COOKIE_HEADER_NAME)) {
            for (String header : this.headers.get(COOKIE_HEADER_NAME)) {
                for (String cookie : header.split(";")) {
                    cookie = cookie.trim();
                    int index = cookie.indexOf('=');
                    if (index >= 0) {
                        this.cookies.put(cookie.substring(0, index), cookie.substring(index + 1));
                    }
                }
            }
        }
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
        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());

        return new WebRequest(exchange.getRequestURI().getPath(),
                method,
                exchange.getRequestHeaders(),
                parameters(exchange.getRequestURI().getQuery()),
                method == HttpMethod.POST ? parameters(exchange.getRequestBody()) : Collections.emptyMap()
        );
    }
}
