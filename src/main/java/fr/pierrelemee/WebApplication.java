package fr.pierrelemee;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebApplication implements HttpHandler {

    protected final int port;
    protected Router router;

    public WebApplication(int port) {
        this.port = port;
        this.router = new Router();
    }

    public void addController(Controller controller) {

        for (Route route: controller.routes()) {
            this.router.addRoute(route);
        }
    }

    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("Requested: " + exchange.getRequestURI());
            int status = 200;
            String body;

            WebRequest request = WebRequest.fromExchange(exchange);
            Route route = this.router.match(request);

            if (null != route) {
                try {
                    WebResponse response = route.getProcess().process(request);
                    body = response.getBody();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    status = 500;
                    body = "Internal server error";
                }
            } else {
                status = 404;
                body = "Not found";
            }

            exchange.sendResponseHeaders(status, body.getBytes().length);
            exchange.getResponseBody().write(body.getBytes());
            exchange.getResponseBody().close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            exchange.sendResponseHeaders(500, "Internal server error".getBytes().length);
            exchange.getResponseBody().write("Internal server error".getBytes());
            exchange.getResponseBody().close();
        }
    }

    public void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(this.port), 0);
        server.createContext("/", this);
        server.setExecutor(null);
        server.start();
    }
}
