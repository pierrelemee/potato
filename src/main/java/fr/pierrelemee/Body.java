package fr.pierrelemee;

import java.io.IOException;
import java.io.OutputStream;

public interface Body {

    void write(OutputStream output, Renderer renderer) throws IOException;
}
