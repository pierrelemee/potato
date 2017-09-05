package fr.pierrelemee;

import java.io.OutputStream;

public interface Renderer {

    void render(OutputStream output, TemplateBody template);
}
