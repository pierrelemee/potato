package fr.pierrelemee;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateBody implements Body {

    protected String path;
    protected Map<String, Object> parameters;

    private TemplateBody(String path) {
        this.path = path;
        this.parameters = new LinkedHashMap<>();
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public TemplateBody addParameter(String name, Object value) {
        this.parameters.put(name, value);

        return this;
    }

    @Override
    public TemplateBody addParameters(Map<String, Object> parameters) {
        this.parameters.putAll(parameters);

        return this;
    }

    @Override
    public void write(OutputStream output, Renderer renderer) throws IOException {
        renderer.render(output, this);
    }

    public static TemplateBody fromPath(String path) {
        return new TemplateBody(path);
    }

    public static TemplateBody fromPath(String path, Map<String, Object> parameters) {
        TemplateBody template = new TemplateBody(path);

        return template.addParameters(parameters);
    }
}
