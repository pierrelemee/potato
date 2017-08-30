package fr.pierrelemee;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fr.pierrelemee.route.RouteMatching;
import fr.pierrelemee.route.RouterException;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebApplication implements HttpHandler {

    private static final Integer DEFAULT_PORT = 8123;

    protected Router router;
    protected SessionManager sessionManager;

    public WebApplication() {
        this(new Router());
    }

    public WebApplication(Router router) {
        this(router, null);
    }

    public WebApplication(SessionManager sessionManager) {
        this(new Router(), sessionManager);
    }

    public WebApplication(Router router, SessionManager sessionManager) {
        this.router = router;
        this.sessionManager = sessionManager;
    }

    public void addController(Controller controller) throws RouterException {

        for (Route route : controller.routes()) {
            this.router.addRoute(route);
        }
    }

    public void handle(HttpExchange exchange) throws IOException {
        WebResponse response = this.process(WebRequest.fromExchange(exchange));

        exchange.getResponseHeaders().putAll(response.getHeaders());
        exchange.sendResponseHeaders(response.getStatus(), response.getBody().getBytes().length);
        exchange.getResponseBody().write(response.getBody().getBytes());
        exchange.getResponseBody().close();
    }

    public WebResponse process(WebRequest request) {
        System.out.println("Requested: " + request.getPath());

        Session session = this.sessionManager != null ? this.sessionManager.extract(request) : null;
        WebResponse response = this.getResponse(request, session);

        if (session != null && !session.isSent()) {
            response.addCookie(Cookie.Builder.create(this.sessionManager.getSessionCookieName()).setValue(session.getHash()).build());
        }

        return response;
    }

    protected WebResponse getResponse(WebRequest request, Session session) {
        RouteMatching matching = this.router.match(request);

        if (matching.hasRoute()) {
            try {
                request.addVariables(matching.getVariables());
                return matching.getRoute().getProcess().process(request, session);

            } catch (Exception e) {
                e.printStackTrace();
                return WebResponse
                    .status(500)
                    .writeBody("Internal server error");
            }
        }

        return WebResponse
            .status(404)
            .writeBody("Not found");
    }

    public void start() throws Exception {
        this.start(DEFAULT_PORT);
    }

    public void start(int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", this);
        server.setExecutor(null);
        server.start();
    }
}
