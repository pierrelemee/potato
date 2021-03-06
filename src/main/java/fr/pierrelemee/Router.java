package fr.pierrelemee;

import fr.pierrelemee.route.RouteMatching;
import fr.pierrelemee.route.RouteTree;
import fr.pierrelemee.route.RouterException;

import java.util.*;

public class Router {

    protected Map<String, Route> registry;
    protected Map<HttpMethod, RouteTree> routes;

    public Router() {
        this.registry = new LinkedHashMap<>();
        this.routes = new LinkedHashMap<>();
    }

    public void addRoute(Route route) throws RouterException {
        if (this.registry.containsKey(route.getName())) {
            throw new RouterException("Conflicting route name for " + route.getName());
        }

        this.registry.put(route.getName(), route);

        if (!this.routes.containsKey(route.getMethod())) {
            this.routes.put(route.getMethod(), new RouteTree());
        }

        if (this.routes.get(route.getMethod()).hasRoute(route)) {
            throw new RouterException("Conflicting route path for " + route.getName() + " (" + route.getPath() + ")");
        }

        this.routes.get(route.getMethod()).addRoute(route);
    }

    public Route findRouteByName(String name) {
        if (this.registry.containsKey(name)) {
            return this.registry.get(name);
        }

        return null;
    }

    public String url(String name) {
        return this.url(name, Collections.emptyMap());
    }

    public String url(String name, Map<String, Object> parameters) {
        Route target = this.findRouteByName(name);


        if (null != target) {
            List<String> url = new LinkedList<>();
            for (String element: RouteTree.buildPathElements(target.getPath())) {
                if (element.matches("<.*>")) {
                    if (parameters.containsKey(element.substring(1, element.length() - 1))) {
                        url.add(parameters.get(element.substring(1, element.length() - 1)).toString());
                    } else {
                        url.add("");
                    }
                } else {
                    url.add(element);
                }
            }

            return Route.SEPARATOR + String.join(Route.SEPARATOR, url);
        }

        return "";
    }

    public RouteMatching match(WebRequest request) {
        if (this.routes.containsKey(request.getMethod())) {
            return this.routes.get(request.getMethod()).getMatchingRoute(request);
        }

        return RouteMatching.none();
    }
}
