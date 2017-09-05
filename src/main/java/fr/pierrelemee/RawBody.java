package fr.pierrelemee;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class RawBody implements Body {

    protected String content;

    private RawBody(String content) {
        this.content = content;
    }

    @Override
    public RawBody addParameter(String name, Object value) {
        return this;
    }

    @Override
    public RawBody addParameters(Map<String, Object> parameters) {
        return this;
    }

    @Override
    public void write(OutputStream output, Renderer renderer) throws IOException {
        output.write(this.content.getBytes());
    }

    public static RawBody fromContent(String content) {
        return new RawBody(content);
    }
}
