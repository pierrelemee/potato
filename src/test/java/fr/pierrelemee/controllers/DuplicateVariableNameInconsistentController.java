package fr.pierrelemee.controllers;

import fr.pierrelemee.Controller;
import fr.pierrelemee.WebResponse;
import fr.pierrelemee.annotations.Route;

public class DuplicateVariableNameInconsistentController extends Controller {

    @Route(name = "inconsistent_a_a", path = "/inconsistent/<a>/<a>")
    public WebResponse a() {
        return new WebResponse("Inconsistent a a");
    }
}
