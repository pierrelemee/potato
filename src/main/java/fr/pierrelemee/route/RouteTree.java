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
                String variable = elements.get(0).substring(1, elements.get(0).length() - 1);
                if (this.variable != null) {
                    throw new RouterException("Conflicting variable " + variable + " with " + this.variable);
                }

                if (this.findVariable(variable)) {
                    throw new RouterException("Duplicate variable " + variable + " in tree");
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

    protected boolean findVariable(String name) {
        if (this.parent != null) {
            return this.parent.variable != null && this.parent.variable.equalsIgnoreCase(name);
        }

        return false;
    }

    public final RouteMatching getMatchingRoute(WebRequest request) {
        return this.getMatchingRoute(buildPathElements(request.getPath()), new LinkedHashMap<>());
    }

    protected RouteMatching getMatchingRoute(List<String> elements, Map<String, String> variables) {
        if (elements.isEmpty()) {
            return new RouteMatching(this.route, variables);
        }

        if (this.children.containsKey(elements.get(0))) {
            return this.children.get(elements.get(0)).getMatchingRoute(elements.subList(1, elements.size()), variables);
        }

        if (this.variable != null) {
            variables.put(this.variable, elements.get(0));
            return this.child != null ? this.child.getMatchingRoute(elements.subList(1, elements.size()), variables) : null;
        }

        return RouteMatching.none();
    }

    static List<String> buildPathElements(String path) {
        return Arrays.stream(path.split(Route.SEPARATOR)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }
}
