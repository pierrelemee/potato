package fr.pierrelemee;

import fr.pierrelemee.controllers.*;
import fr.pierrelemee.route.RouterException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void testSimpleController() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/test");

        assertEquals(200, response.getStatus());
        assertEquals("test - index", response.getBody());
        assertTrue(response.getHeaders().containsKey("Foo"));
        assertTrue(response.getHeaders().get("Foo").contains("bar"));
    }

    @Test
    public void testGetWithSimplePathVariable() throws RouterException {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/test/hello/Chuck");

        assertEquals(200, response.getStatus());
        assertEquals("test - hello Chuck !", response.getBody());
    }

    @Test
    public void testGetWithComplexPathVariable() throws RouterException {
        WebApplication app = new WebApplication();
        app.addController(new CalculatorController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/calculator/5/2/sum");

        assertEquals(200, response.getStatus());
        assertEquals("Sum is 7", response.getBody());
    }

    @Test
    public void testCookieStorageController() throws RouterException {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        WebResponse response = client.get("/test/cookie");

        assertEquals(200, response.getStatus());
        assertEquals("no foo", response.getBody());

        response = client.get("/test/cookie");

        assertEquals(200, response.getStatus());
        System.out.println(response.getBody());
        assertTrue("Unexpected response body on 2nd call", response.getBody().matches("foo = bar[0-9]{1}"));
    }

    @Test
    public void testSimpleNotFoundController() throws Exception {

    }

    @Test(expected = RouterException.class)
    public void testConflictingVariableInconsistentController() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new ConflictingVariableInconsistentController());
    }

    @Test(expected = RouterException.class)
    public void testDuplicateVariableNameInconsistentController() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new DuplicateVariableNameInconsistentController());
    }

    @Test(expected = RouterException.class)
    public void testDuplicateRouteNameInconsistentController() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new DuplicateRouteNameInconsistentController());
    }

    @Test
    public void testGetRouteByeName() throws Exception {
        Router router = new Router();

        WebApplication app = new WebApplication(router);
        app.addController(new TestController());

        Route route = router.findRouteByName("test_index");
        assertNotNull(route);
        assertEquals("/test", route.getPath());
        assertEquals(HttpMethod.GET, route.getMethod());
    }
}
