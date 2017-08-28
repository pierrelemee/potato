package fr.pierrelemee;


import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebRequest {

    protected HttpMethod method;
    protected String path;
    protected Map<String, String> headers;
    protected Map<String, List<String>> get;
    protected Map<String, List<String>> post;

    public WebRequest(String path) {
        this(path, HttpMethod.GET);
    }

    public WebRequest(String path, HttpMethod method) {
        this(path, method, Collections.emptyMap());
    }

    public WebRequest(String path, HttpMethod method, Map<String, List<String>> get) {
        this.path = path;
        this.method = method;
        this.get = get;
        this.headers = Collections.emptyMap();
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
        System.out.println(exchange.getRequestURI().getPath());

        return new WebRequest(exchange.getRequestURI().getPath(),
                HttpMethod.valueOf(exchange.getRequestMethod()),
                parameters(exchange.getRequestURI().getQuery())
                );
    }
}
