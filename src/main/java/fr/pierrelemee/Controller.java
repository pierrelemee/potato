package fr.pierrelemee;

import java.lang.reflect.Method;
import java.util.*;

public abstract class Controller {

    public Map<Route, RequestProcess> routes() {
        Map<Route, RequestProcess> routes = new LinkedHashMap<>();

        for (Method method: this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(fr.pierrelemee.annotations.Route.class)) {
                if (method.getReturnType().equals(WebResponse.class)) {
                    routes.put(Route.fromAnnotation(method.getAnnotation(fr.pierrelemee.annotations.Route.class)), new ControllerRequestProcess(this, method));
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
