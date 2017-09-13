package fr.pierrelemee.route;

import fr.pierrelemee.Route;
import fr.pierrelemee.WebRequest;

import java.util.*;
import java.util.stream.Collectors;

public class RouteTree {

    private static final String VARIABLE_PATTERN = "<.*>";

    protected RouteTree parent;
    protected Route route;
    protected Map<String, RouteTree> children;

    public RouteTree() {
        this(null);
    }

    protected RouteTree(RouteTree parent) {
        this.parent = parent;
        this.children = new LinkedHashMap<>();
    }

    public final boolean hasRoute(Route route) {
        return this.hasRoute(buildPathElements(route.getPath()));
    }

    public final boolean hasRoute(List<String> elements) {
        if (elements.isEmpty()) {
            return this.route != null;
        }

        if (elements.get(0).matches(VARIABLE_PATTERN)) {
            for (String key: this.children.keySet()) {
                if (key.matches(VARIABLE_PATTERN) && children.get(key).hasRoute(elements.subList(1, elements.size()))) {
                    return true;
                }
            }
        } else {
            return this.children.containsKey(elements.get(0)) && this.children.get(elements.get(0)).hasRoute(elements.subList(1, elements.size()));
        }

        return false;
    }

    public final void addRoute(Route route) throws RouterException {
        List<String> elements = buildPathElements(route.getPath());

        List<String> duplicates = elements.stream().filter(e -> e.matches(VARIABLE_PATTERN)).filter(i -> Collections.frequency(elements, i) >1).collect(Collectors.toList());

        if (!duplicates.isEmpty()) {
            throw new RouterException(String.format("Duplicate variable(s) [%s] in path %s", Arrays.toString(duplicates.toArray()), route.getPath()));
        }

        this.addRoute(route, buildPathElements(route.getPath()));
    }

    protected void addRoute(Route route, List<String> elements) throws RouterException {
        if (elements.isEmpty()) {
            this.route = route;
        } else {
            /*
            if (elements.get(0).matches(VARIABLE_PATTERN)) {
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
             */

            if (!this.children.containsKey(elements.get(0))) {
                this.children.put(elements.get(0), new RouteTree(this));
            }

            this.children.get(elements.get(0)).addRoute(route, elements.subList(1, elements.size()));
        }
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

        RouteMatching matching;
        for (String key: this.children.keySet()) {
            if (key.matches(VARIABLE_PATTERN) && null != (matching = this.children.get(key).getMatchingRoute(elements.subList(1, elements.size()), variables))) {
                variables.put(key.substring(1, key.length() -1), elements.get(0));
                return matching;
            }
        }

        return RouteMatching.none();
    }

    public static List<String> buildPathElements(String path) {
        return Arrays.stream(path.split(Route.SEPARATOR)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }
}
