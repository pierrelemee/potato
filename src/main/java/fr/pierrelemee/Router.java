package fr.pierrelemee;

import fr.pierrelemee.route.RouteMatching;
import fr.pierrelemee.route.RouteTree;
import fr.pierrelemee.route.RouterException;

import java.util.*;

public class Router {

    protected Map<HttpMethod, RouteTree> routes;

    public Router() {
        this.routes = new LinkedHashMap<>();
    }

    public void addRoute(Route route) throws RouterException {
        if (!this.routes.containsKey(route.getMethod())) {
            this.routes.put(route.getMethod(), new RouteTree());
        }

        this.routes.get(route.getMethod()).addRoute(route);
    }

    public RouteMatching match(WebRequest request) {
        if (this.routes.containsKey(request.getMethod())) {
            return this.routes.get(request.getMethod()).getMatchingRoute(request);
        }

        return RouteMatching.none();
    }
}
