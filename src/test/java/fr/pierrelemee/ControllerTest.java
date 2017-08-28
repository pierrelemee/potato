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

}
