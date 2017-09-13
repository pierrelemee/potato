package fr.pierrelemee;

import fr.pierrelemee.controllers.*;
import fr.pierrelemee.run.renderers.RawRenderer;
import fr.pierrelemee.route.RouterException;
import fr.pierrelemee.sessions.InMemorySessionManager;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void testSimpleController() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/");

        assertEquals(200, response.getStatus());
        assertEquals("test - index", client.getBody());
        assertTrue(response.getHeaders().containsKey("Foo"));
        assertTrue(response.getHeaders().get("Foo").contains("bar"));
    }

    @Test
    public void testRedirectionController() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/test/forbidden");

        assertEquals(301, response.getStatus());
        assertTrue(response.getHeaders().containsKey("Location"));
        assertTrue(response.getHeaders().get("Location").contains("/test"));

        response = client.get("/test/profile");

        assertEquals(302, response.getStatus());
        assertTrue(response.getHeaders().containsKey("Location"));
        assertTrue(response.getHeaders().get("Location").contains("/test"));
    }

    @Test
    public void testGetWithSimplePathVariable() throws RouterException {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/test/hello/Chuck");

        assertEquals(200, response.getStatus());
        assertEquals("test - hello Chuck !", client.getBody());
    }

    @Test
    public void testGetWithComplexPathVariable() throws RouterException {
        WebApplication app = new WebApplication();
        app.addController(new CalculatorController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/calculator/5/2/sum");

        assertEquals(200, response.getStatus());
        assertEquals("Sum is 7", client.getBody());
    }

    @Test
    public void testPostParameters() throws RouterException {
        WebApplication app = new WebApplication();
        app.addController(new CalculatorController());

        MockClient client = new MockClient(app);
        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("a", Integer.toString(3));
        parameters.put("b", Integer.toString(6));

        WebResponse response = client.post("/calculator/sum", parameters);

        assertEquals(200, response.getStatus());
        assertEquals("Sum is 9", client.getBody());
    }

    @Test
    public void testCookieStorageController() throws RouterException {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/test/cookie");

        assertEquals(200, response.getStatus());
        assertEquals("no foo", client.getBody());

        response = client.get("/test/cookie");

        assertEquals(200, response.getStatus());
        assertTrue("Unexpected response body on 2nd call", client.getBody().matches("foo = bar[0-9]{1}"));
    }

    @Test
    public void testSessionController() throws RouterException {
        SessionManager sessionManager = new InMemorySessionManager();
        WebApplication app = new WebApplication(sessionManager);
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/test/session");

        assertEquals(200, response.getStatus());
        assertEquals("counter: 1", client.getBody());

        response = client.get("/test/session");

        assertEquals(200, response.getStatus());
        assertEquals("counter: 2", client.getBody());
    }

    @Test
    public void testSimpleNotFoundController() throws Exception {
        WebApplication app = new WebApplication();

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/nowhere");

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testTemplateController() throws Exception {
        WebApplication app = new WebApplication(new RawRenderer());
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/test/template");

        assertEquals(200, response.getStatus());
        assertEquals("This is a template", client.getBody());
    }
}
