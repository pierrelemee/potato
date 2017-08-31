package fr.pierrelemee.renderers;

import fr.pierrelemee.RenderTemplate;
import fr.pierrelemee.Renderer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RawRenderer implements Renderer {

    @Override
    public String render(RenderTemplate template) {
        try {
            URL resource = this.getClass().getResource(template.getPath());
            return resource != null ? new String(Files.readAllBytes(Paths.get(resource.toURI()))) : null;
        } catch (URISyntaxException|IOException e) {
            return null;
        }
    }
}
