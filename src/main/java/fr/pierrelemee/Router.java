package fr.pierrelemee;

import java.util.LinkedHashMap;
import java.util.Map;

public class Router {

    protected Map<HttpMethod, Map<String, Route>> routes;

    public Router() {
        this.routes = new LinkedHashMap<>();
    }

    public void addRoute(Route route) {
        if (!this.routes.containsKey(route.getMethod())) {
            this.routes.put(route.getMethod(), new LinkedHashMap<>());
        }

        this.routes.get(route.getMethod()).put(route.getPath(), route);
    }

    public Route match(WebRequest request) {
        if (this.routes.containsKey(request.getMethod())) {
            if (this.routes.get(request.getMethod()).containsKey(request.getPath())) {
                return this.routes.get(request.getMethod()).get(request.getPath());
            }
        }

        return null;
    }

    private class RouteTree {

    }
}
