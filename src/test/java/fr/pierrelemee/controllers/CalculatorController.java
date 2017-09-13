package fr.pierrelemee.controllers;

import fr.pierrelemee.Controller;
import fr.pierrelemee.HttpMethod;
import fr.pierrelemee.WebRequest;
import fr.pierrelemee.WebResponse;
import fr.pierrelemee.annotations.Route;

public class CalculatorController extends Controller {

    @Route(name = "calculator_sum_path", path = "/calculator/<a>/<b>/sum")
    public WebResponse sum_path(WebRequest request) {
        Integer a = Integer.parseInt(request.variable("a"));
        Integer b = Integer.parseInt(request.variable("b"));

        return new WebResponse("Sum is " + (a + b));
    }

    @Route(name = "calculator_multi_path", path = "/calculator/<a>/<b>/multi")
    public WebResponse multi_path(WebRequest request) {
        Integer a = Integer.parseInt(request.variable("a"));
        Integer b = Integer.parseInt(request.variable("b"));

        return new WebResponse("Product is " + (a * b));
    }

    @Route(name = "calculator_sum_post", path = "/calculator/sum", method = HttpMethod.POST)
    public WebResponse sum_post(WebRequest request) {
        return new WebResponse(
            request.post().has("a") && request.post().has("b") ?
                "Sum is " + (request.post().getInteger("a") + request.post().getInteger("b")) :
                "Missing arguments a and b"
        );
    }
}
