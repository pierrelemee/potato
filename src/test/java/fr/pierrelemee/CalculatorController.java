package fr.pierrelemee;

import fr.pierrelemee.annotations.Route;

public class CalculatorController extends Controller {

    @Route(name = "calculator_sum", path = "/calculator/<a>/<b>/sum")
    public WebResponse hello(WebRequest request) {
        Integer a = Integer.parseInt(request.variable("a"));
        Integer b = Integer.parseInt(request.variable("b"));

        return new WebResponse("Sum is " + (a + b));
    }
}
