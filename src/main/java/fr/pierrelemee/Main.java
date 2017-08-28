package fr.pierrelemee;

import fr.pierrelemee.controllers.TestController;

public class Main {

    public static void main(String... args) throws Exception {
        WebApplication app = new WebApplication(8123);
        app.addController(new TestController());
        app.start();
    }
}