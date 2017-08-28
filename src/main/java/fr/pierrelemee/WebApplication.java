package fr.pierrelemee;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebApplication implements HttpHandler {

    protected final int port;
    protected Map<Route, RequestProcess> routes = new LinkedHashMap<>();

    public WebApplication(int port) {
        this.port = port;
    }

    public void addController(Controller controller) {
        this.routes.putAll(controller.routes());
    }

    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("Requested: " + exchange.getRequestURI());
            int status = 200;
            String body = "";
            boolean found = false;

            exchange.getRequestURI().getQuery();

            WebRequest request = WebRequest.fromExchange(exchange);

            for (Route route : routes.keySet()) {
                if (route.getPath().equalsIgnoreCase(exchange.getRequestURI().getPath())) {
                    try {
                        WebResponse response = this.routes.get(route).process(request);
                        body = response.getBody();

                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                        status = 500;
                        body = "Internal server error";
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                status = 404;
                body = "Not found";
            }
            
            exchange.sendResponseHeaders(status, body.getBytes().length);
            exchange.getResponseBody().write(body.getBytes());
            exchange.getResponseBody().close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(this.port), 0);
        server.createContext("/", this);
        server.setExecutor(null);
        server.start();
    }
}
