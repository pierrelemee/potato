package fr.pierrelemee;

import fr.pierrelemee.annotations.Route;

public class InconsistentController extends Controller {

    @Route(name = "inconsistent_a", path = "/inconsistent/<a>")
    public WebResponse a() {
        return new WebResponse("Inconsistent a");
    }

    @Route(name = "inconsistent_b", path = "/inconsistent/<b>")
    public WebResponse b() {
        return new WebResponse("Inconsistent b");
    }
}
