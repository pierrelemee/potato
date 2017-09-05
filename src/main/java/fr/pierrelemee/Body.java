package fr.pierrelemee;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface Body {

    void write(OutputStream output, Renderer renderer) throws IOException;

    public Body addParameter(String name, Object value);

    public Body addParameters(Map<String, Object> parameters);
}
