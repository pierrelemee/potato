package fr.pierrelemee;

import org.junit.Test;
import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void testSimpleController() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        MockClient.MockExchange exchange = client.get("/test");

        assertEquals(200, exchange.getResponseCode());
        assertEquals("test - index", exchange.getResponseBodyString());
    }

    @Test
    public void testGetWithSimplePathVariable() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        MockClient.MockExchange exchange = client.get("/test/hello/Chuck");

        assertEquals(200, exchange.getResponseCode());
        assertEquals("test - hello Chuck !", exchange.getResponseBodyString());
    }

    @Test
    public void testGetWithComplexPathVariable() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new CalculatorController());

        MockClient client = new MockClient(app);

        MockClient.MockExchange exchange = client.get("/calculator/5/2/sum");

        assertEquals(200, exchange.getResponseCode());
        assertEquals("Sum is 7", exchange.getResponseBodyString());
    }

    @Test
    public void testSimpleNotFoundController() throws Exception {
        WebApplication app = new WebApplication();
        app.addController(new TestController());

        MockClient client = new MockClient(app);

        MockClient.MockExchange exchange = client.post("/test");

        assertEquals(404, exchange.getResponseCode());
        assertEquals("Not found", exchange.getResponseBodyString());
    }

}
