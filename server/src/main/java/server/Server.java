package server;

import com.google.gson.Gson;
import io.javalin.*;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.*;

public class Server {

    private final Javalin javalin;
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(gameDAO, authDAO);
    private final DeleteService deleteService = new DeleteService(userDAO, authDAO, gameDAO);
    private final Gson gson = new Gson();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", ctx -> { // This is a lot of pet shop copy paste and stack overflow examples. Using a lambda just like lecture.

            try {

                UserData userData = gson.fromJson(ctx.body(), UserData.class);
                AuthData authData = userService.register(userData);
                ctx.result(gson.toJson(authData));
            }
            catch (BadRequestException ex){ // Basing my exception logic on the phase three documentation.

            }
            catch (AlreadyTakenException ex){

            }
            catch (Exception ex){

            }
        });

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
