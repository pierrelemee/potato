package fr.pierrelemee;

public class Route {

    public static final String SEPARATOR = "/";

    protected String name;
    protected HttpMethod method;
    protected String path;
    protected RequestProcess process;

    public Route(String name, HttpMethod method, String path) {
        this.name = name;
        this.method = method;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public RequestProcess getProcess() {
        return process;
    }

    public void setProcess(RequestProcess process) {
        this.process = process;
    }

    public static Route fromAnnotation(fr.pierrelemee.annotations.Route annotation) {
        return new Route(annotation.name(), annotation.method(), annotation.path());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Route) {
            return this.name.equalsIgnoreCase(((Route) obj).name);
        }

        return false;
    }
}
