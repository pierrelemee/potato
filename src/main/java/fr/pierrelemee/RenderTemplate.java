package fr.pierrelemee;

import java.util.LinkedHashMap;
import java.util.Map;

public class RenderTemplate {

    protected String path;
    protected Map<String, Object> parameters;

    public RenderTemplate(String path) {
        this.path = path;
        this.parameters = new LinkedHashMap<>();
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public RenderTemplate addParameter(String name, Object value) {
        this.parameters.put(name, value);

        return this;
    }
}
