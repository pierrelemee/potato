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
    protected Parameters parameters;
    protected Parameters get;
    protected Parameters post;

    public WebRequest(String path) {
        this(path, HttpMethod.GET);
    }

    public WebRequest(String path, HttpMethod method) {
        this(path, method, Parameters.empty(), new LinkedHashMap<>());
    }

    public WebRequest(String path, HttpMethod method, Map<String, List<String>> parameters) {
        this(path, method, new Parameters(parameters));
    }

    public WebRequest(String path, HttpMethod method, Parameters parameters) {
        this(path, method, parameters, new LinkedHashMap<>());
    }

    public WebRequest(String path, HttpMethod method, Parameters parameters, Map<String, List<String>> headers) {
        this(path, method, method != HttpMethod.POST ? parameters : Parameters.empty(), method == HttpMethod.POST ? parameters : Parameters.empty(), headers);
    }

    public WebRequest(String path, HttpMethod method, Map<String, List<String>> parameters, Map<String, List<String>> headers) {
        this(path, method, method != HttpMethod.POST ? new Parameters(parameters) : Parameters.empty(), method == HttpMethod.POST ? new Parameters(parameters) : Parameters.empty(), headers);
    }

    public WebRequest(String path, HttpMethod method, Parameters get, Parameters post, Map<String, List<String>> headers) {
        this.path = path;
        this.method = method;
        this.get = get;
        this.post = post;
        this.parameters = this.method == HttpMethod.POST ? this.post : this.get;
        this.variables = new LinkedHashMap<>();
        this.cookies = new LinkedHashMap<>();
        this.headers = headers;

        this.exctractCookies();
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

    public Parameters parameters() {
        return this.get;
    }

    public Parameters get() {
        return this.get;
    }

    public Parameters post() {
        return this.post;
    }

    public static WebRequest fromExchange(HttpExchange exchange) {
        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());

        return new WebRequest(exchange.getRequestURI().getPath(),
                method,
                Parameters.fromString(exchange.getRequestURI().getQuery()),
                method == HttpMethod.POST ? Parameters.fromInputStream(exchange.getRequestBody()) : Parameters.empty(),
                exchange.getRequestHeaders()
        );
    }
}
