package fr.pierrelemee;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Controller {

    protected Renderer renderer;

    public Controller() {
        this(null);
    }

    public Controller(Renderer renderer) {
        this.renderer = renderer;
    }

    public List<Route> routes() {
        List<Route> routes = new LinkedList<>();

        for (Method method : this.getClass().getDeclaredMethods()) {
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
    */
}
