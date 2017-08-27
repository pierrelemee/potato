package fr.pierrelemee;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebApplication implements HttpHandler {

    protected final int port;

    public WebApplication(int port) {
        this.port = port;
    }

    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Requested: " + exchange.getRequestURI());

        exchange.sendResponseHeaders(200, "Coucou".getBytes().length);
        exchange.getResponseBody().write("Coucou".getBytes());
        exchange.getResponseBody().close();
    }

    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(this.port), 0);
        server.createContext("/", this);
        server.start();
    }
}
