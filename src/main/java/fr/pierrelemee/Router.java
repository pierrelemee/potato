package fr.pierrelemee;

import fr.pierrelemee.route.RouteTree;

import java.util.*;

public class Router {

    private static final String SEPARATOR = "/";

    protected Map<HttpMethod, RouteTree> routes;

    public Router() {
        this.routes = new LinkedHashMap<>();
    }

    public void addRoute(Route route) {
        if (!this.routes.containsKey(route.getMethod())) {
            this.routes.put(route.getMethod(), new RouteTree());
        }

        this.routes.get(route.getMethod()).addRoute(route);
    }

    public Route match(WebRequest request) {
        if (this.routes.containsKey(request.getMethod())) {
            return this.routes.get(request.getMethod()).getMatchingRoute(request);
        }

        return null;
    }
}
