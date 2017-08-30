package fr.pierrelemee.controllers;

import fr.pierrelemee.Controller;
import fr.pierrelemee.Cookie;
import fr.pierrelemee.WebRequest;
import fr.pierrelemee.WebResponse;
import fr.pierrelemee.annotations.Route;
import fr.pierrelemee.sessions.SimpleSession;

import java.time.temporal.ChronoUnit;
import java.util.Random;

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

    @Route(name = "test_cookie", path = "/test/cookie")
    public WebResponse cookie(WebRequest request) {
        if (request.hasCookie("foo")) {
            return WebResponse
                .ok()
                    .writeBody("foo = " + request.cookie("foo"));
        } else {
            return WebResponse
                .ok()
                .writeBody("no foo")
                .addCookie(Cookie.Builder
                    .create("foo")
                    .setValue("bar" + (new Random().nextInt(10)))
                    .setPath("/")
                    .setExpires(5, ChronoUnit.MINUTES)
                    .build()
                );
        }
    }

    @Route(name = "test_session", path = "/test/session")
    public WebResponse session(SimpleSession session) {
        return WebResponse
            .ok()
            .writeBody("counter: " + session.increment());
    }
}
