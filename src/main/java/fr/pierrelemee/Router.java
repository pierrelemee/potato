package fr.pierrelemee;

import java.util.*;
import java.util.stream.Collectors;

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
            return this.routes.get(request.getMethod()).match(request);
        }

        return null;
    }

    private static class RouteTree {

        protected Route route;
        protected Map<String, RouteTree> children;

        RouteTree() {
            this(null);
        }

        private RouteTree(Route route) {
            this.route = route;
            this.children = new HashMap<>();
        }

        public void addRoute(Route route) {
            this.addRoute(route, Arrays.stream(route.getPath().split(SEPARATOR)).filter(s -> !s.isEmpty()).collect(Collectors.toList()));
        }

        protected void addRoute(Route route, List<String> elements) {
            if (elements.isEmpty()) {
                this.route = route;
            } else {
                if (!this.children.containsKey(elements.get(0))) {
                    this.children.put(elements.get(0), new RouteTree());
                }

                this.children.get(elements.get(0)).addRoute(route, elements.subList(1, elements.size()));
            }
        }

        public Route match(WebRequest request) {
            return this.match(Arrays.stream(request.getPath().split(SEPARATOR)).filter(s -> !s.isEmpty()).collect(Collectors.toList()));
        }

        protected Route match(List<String> elements) {
            if (elements.isEmpty()) {
                return this.route;
            }

            if (this.children.containsKey(elements.get(0))) {
                return this.children.get(elements.get(0)).match(elements.subList(1, elements.size()));
            }

            return null;
        }
    }
}
