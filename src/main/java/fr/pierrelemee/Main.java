package fr.pierrelemee;

public class Main {

    public static void main(String... args) throws Exception {
        WebApplication app = new WebApplication(8123);
        app.start();
    }
}
