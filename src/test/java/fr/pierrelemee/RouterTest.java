package fr.pierrelemee;

import fr.pierrelemee.controllers.*;
import fr.pierrelemee.route.RouterException;
import fr.pierrelemee.run.renderers.RawRenderer;
import fr.pierrelemee.sessions.InMemorySessionManager;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RouterTest {

    @Test
    public void testRouterGetRouteByName() throws Exception {
        Router router = new Router();
        WebApplication app = new WebApplication(router);
        app.addController(new CalculatorController());

        Route target = router.findRouteByName("calculator_sum_path");

        assertNotNull(target);
        assertEquals("/calculator/<a>/<b>/sum", target.getPath());
    }

    @Test
    public void testRouterGetUrl() throws Exception {
        Router router = new Router();
        WebApplication app = new WebApplication(router);
        app.addController(new CalculatorController());

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("a", 2);
        parameters.put("b", 3L);
        String url = router.url("calculator_sum_path", parameters);

        assertNotNull(url);
        assertEquals("/calculator/2/3/sum", url);
    }
}
