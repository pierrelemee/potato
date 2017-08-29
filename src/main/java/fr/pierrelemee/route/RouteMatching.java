package fr.pierrelemee.route;

import fr.pierrelemee.Route;

import java.util.Collections;
import java.util.Map;

public class RouteMatching {

    protected Route route;
    protected Map<String, String> variables;

    private RouteMatching(Route route) {
        this(null, Collections.emptyMap());
    }

    public RouteMatching(Route route, Map<String, String> variables) {
        this.route = route;
        this.variables = variables;
    }

    public boolean hasRoute() {
        return this.route != null;
    }

    public Route getRoute() {
        return route;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public static RouteMatching none() {
        return new RouteMatching(null);
    }
}
