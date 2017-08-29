package fr.pierrelemee.route;

import fr.pierrelemee.Route;
import fr.pierrelemee.WebRequest;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RouteTree {

    private static final String VARIABLE_PATTERN = "<.*>";

    protected RouteTree parent;
    protected Route route;
    protected String variable;
    protected RouteTree child;
    protected Map<String, RouteTree> children;

    public RouteTree() {
        this(null);
    }

    protected RouteTree(RouteTree parent) {
        this.parent = parent;
        this.children = new LinkedHashMap<>();
    }

    public final void addRoute(Route route) throws RouterException {
        this.addRoute(route, buildPathElements(route.getPath()));
    }

    protected void addRoute(Route route, List<String> elements) throws RouterException {
        if (elements.isEmpty()) {
            this.route = route;
        } else {
            if (elements.get(0).matches(VARIABLE_PATTERN)) {
                if (this.variable != null) {
                    throw new RouterException("Conflicting variable " + elements.get(0) + " with " + this.variable);
                }

                this.variable = elements.get(0).substring(1, elements.get(0).length() - 1);
                this.child = new RouteTree(this);
                this.child.addRoute(route, elements.subList(1, elements.size()));
            } else {
                if (!this.children.containsKey(elements.get(0))) {
                    this.children.put(elements.get(0), new RouteTree(this));
                }

                this.children.get(elements.get(0)).addRoute(route, elements.subList(1, elements.size()));
            }
        }
    }

    public final Route getMatchingRoute(WebRequest request) {
        return this.getMatchingRoute(request, buildPathElements(request.getPath()));
    }

    protected Route getMatchingRoute(WebRequest request, List<String> elements) {
        if (elements.isEmpty()) {
            return this.route;
        }

        if (this.children.containsKey(elements.get(0))) {
            return this.children.get(elements.get(0)).getMatchingRoute(request, elements.subList(1, elements.size()));
        }

        if (this.variable != null) {
            request.addVariable(this.variable, elements.get(0));
            return this.child != null ? this.child.getMatchingRoute(request, elements.subList(1, elements.size())) : null;
        }

        return null;
    }

    static List<String> buildPathElements(String path) {
        return Arrays.stream(path.split(Route.SEPARATOR)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }
}
