package fr.pierrelemee.controllers;

import fr.pierrelemee.Controller;
import fr.pierrelemee.WebResponse;
import fr.pierrelemee.annotations.Route;

public class TestController extends Controller {

    @Route(name = "test_index", path = "/test")
    public WebResponse index() {
        return new WebResponse("test - index");
    }
}
