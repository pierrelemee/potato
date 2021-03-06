package fr.pierrelemee.controllers;

import fr.pierrelemee.Controller;
import fr.pierrelemee.WebResponse;
import fr.pierrelemee.annotations.Route;

public class DuplicateRouteNameInconsistentController extends Controller {

    @Route(name = "inconsistent_a", path = "/inconsistent")
    public WebResponse a() {
        return new WebResponse("Inconsistent a");
    }

    @Route(name = "inconsistent_a", path = "/inconsistent/<a>")
    public WebResponse aa() {
        return new WebResponse("Inconsistent a");
    }
}
