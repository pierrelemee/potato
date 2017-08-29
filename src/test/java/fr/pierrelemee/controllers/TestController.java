package fr.pierrelemee.controllers;

import fr.pierrelemee.Controller;
import fr.pierrelemee.WebRequest;
import fr.pierrelemee.WebResponse;
import fr.pierrelemee.annotations.Route;

public class TestController extends Controller {

    @Route(name = "test_index", path = "/test")
    public WebResponse index() {
        return WebResponse
                .ok()
                .writeBody("test - index")
                .addHeader("Foo", "bar");
    }

    @Route(name = "test_query", path = "/test/query")
    public WebResponse query(WebRequest request) {
        String name = request.get().containsKey("name") ? request.get().get("name").get(0) : "";
        return new WebResponse("test - query: " + name);
    }

    @Route(name = "test_hello", path = "/test/hello/<name>")
    public WebResponse hello(WebRequest request) {
        return new WebResponse("test - hello " + request.variable("name") + " !");
    }
}
