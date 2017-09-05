package fr.pierrelemee.run.renderers;

import fr.pierrelemee.TemplateBody;
import fr.pierrelemee.Renderer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RawRenderer implements Renderer {

    @Override
    public void render(OutputStream output, TemplateBody template) {
        try {
            URL resource = this.getClass().getResource(template.getPath());
            output.write(Files.readAllBytes(Paths.get(resource.toURI())));
        } catch (URISyntaxException|IOException e) {
            // TODO add some log
        }
    }
}
