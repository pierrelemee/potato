package fr.pierrelemee;

import fr.pierrelemee.controllers.TestController;
import fr.pierrelemee.sessions.InMemorySessionManager;

public class Main {

    public static void main(String... args) throws Exception {
        WebApplication app = new WebApplication(new InMemorySessionManager());
        app.addController(new TestController());
        app.start();
    }
}
