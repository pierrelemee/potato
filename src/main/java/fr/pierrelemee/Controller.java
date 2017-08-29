package fr.pierrelemee;

import java.lang.reflect.Method;
import java.util.*;

public abstract class Controller {

    public List<Route> routes() {
        List<Route> routes = new LinkedList<>();

        for (Method method: this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(fr.pierrelemee.annotations.Route.class)) {
                if (method.getReturnType().equals(WebResponse.class)) {
                    Route route = Route.fromAnnotation(method.getAnnotation(fr.pierrelemee.annotations.Route.class));
                    route.setProcess(new ControllerRequestProcess(this, method));
                    routes.add(route);
                }
            }
        }

        return routes;
    }

    /*
    protected Response<String>  redirect(String location) {
        return this.redirect(location, false);
    }

    protected Response<String> redirect(String location, boolean permanent) {
        return Response
            .of(permanent?Status.MOVED_PERMANENTLY:Status.FOUND, "")
            .withHeader("Location", location);

    }

    protected String render(String resource) {
        return this.renderer.render(resource);
    }

    protected String render(String resource, Renderer.Parameter ... parameters) {
        return this.renderer.render(resource, parameters);
    }

    protected String render(String resource, Map<String, Object> parameters) {
        return this.renderer.render(resource, parameters);
    }
    */
}
