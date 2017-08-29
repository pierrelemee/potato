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
        protected String variable;
        protected Map<String, RouteTree> children;

        RouteTree() {
            this(null);
        }

        private RouteTree(Route route, String variable) {
            this.route = route;
            this.variable = variable;
            this.children = Collections.emptyMap();
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
                if (elements.get(0).matches("<.*>")) {
                    this.variable = elements.get(0).substring(1, elements.get(0).length() - 1);
                    this.route = route;
                    // TODO what if the tree already has any child :o ?
                } else {
                    if (!this.children.containsKey(elements.get(0))) {
                        this.children.put(elements.get(0), new RouteTree());
                    }

                    this.children.get(elements.get(0)).addRoute(route, elements.subList(1, elements.size()));
                }
            }
        }

        public Route match(WebRequest request) {
            return this.match(request, Arrays.stream(request.getPath().split(SEPARATOR)).filter(s -> !s.isEmpty()).collect(Collectors.toList()));
        }

        protected Route match(WebRequest request, List<String> elements) {
            if (elements.isEmpty()) {
                return this.route;
            }

            if (this.variable != null) {
                request.addVariable(this.variable, elements.get(0));
                return this.route;
            }

            if (this.children.containsKey(elements.get(0))) {
                return this.children.get(elements.get(0)).match(request, elements.subList(1, elements.size()));
            }

            return null;
        }
    }
}
