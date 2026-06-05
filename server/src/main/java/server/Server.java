package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        // Okay i am looking through petshop and the textbook to make sure I understand how things work.
        // The lecture videos were good and helped a lot.

        // I think this is a good place to start.
        // I can also go and add all those folders across the project that are going to
        // be required later in the project.

        // Refering back to my project 2 to understand what I need to do.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
