package fr.pierrelemee;

public class Route {

    protected String name;
    protected String path;

    public Route(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public static Route fromAnnotation(fr.pierrelemee.annotations.Route annotation) {
        return new Route(annotation.name(), annotation.path());
    }
}
