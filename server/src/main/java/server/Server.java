package server;

import com.google.gson.Gson;
import io.javalin.*;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.NewPlayer;
import model.UserData;
import service.*;

import java.util.Collection;

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


        /*
        * All of my endpoints are HTTP Route -> deserialized json -> service methods -> serialize the response -> handle exceptions.
        * */

        // Block for registration.
        javalin.post("/user", ctx -> { // This is a lot of pet shop copy paste and stack overflow examples. Using a lambda just like lecture.

            try {

                UserData userData = gson.fromJson(ctx.body(), UserData.class);
                AuthData authData = userService.register(userData);
                ctx.result(gson.toJson(authData));
            }
            catch (BadRequestException ex){ // Basing my exception logic on the phase three documentation.
                ctx.status(400);
                ctx.result(ex.getMessage());
            }
            catch (AlreadyTakenException ex){
                ctx.status(403);
                ctx.result(ex.getMessage());
            }
            catch (Exception ex){
                ctx.status(500);
                ctx.result(ex.getMessage());
            }
        });

        // Block for logging in.
        javalin.post("/session", ctx -> {
            try {
                UserData userData = gson.fromJson(ctx.body(), UserData.class);
                AuthData authData = userService.login(userData);
                ctx.result(gson.toJson(authData));

            }
            catch (BadRequestException ex) {
                ctx.status(400);
                ctx.result(ex.getMessage());
            }
            catch (UnauthorizedException ex) { // This is different from register.
                ctx.status(401);
                ctx.result(ex.getMessage());
            }
            catch (Exception ex) {
                ctx.status(500);
                ctx.result(ex.getMessage());
            }
        });

        // Block for logging out.
        javalin.delete("/session", ctx -> {
            try {
                String authToken = ctx.header("Authorization"); // "Authorization" should grab the token.
                userService.logout(authToken);
                ctx.status(200);
            }
            catch (UnauthorizedException ex) {
                ctx.status(401);
                ctx.result(ex.getMessage());
            }
            catch (Exception ex) {
                ctx.status(500);
                ctx.result(ex.getMessage());
            }
        });

        // List games
        javalin.get("/game", ctx -> {
            try {
                String authToken = ctx.header("Authorization");
                Collection<GameData> games = gameService.listGames(authToken);
                ctx.result(gson.toJson(games));
            }
            catch (UnauthorizedException ex) {
                ctx.status(401);
                ctx.result(ex.getMessage());
            }
            catch (Exception ex) {
                ctx.status(500);
                ctx.result(ex.getMessage());
            }
        });

        // Create game
        javalin.post("/game", ctx -> {
            try {
                String authToken = ctx.header("Authorization");
                GameData gameData = gson.fromJson(ctx.body(), GameData.class);

                int gameID = gameService.createGame(authToken, gameData.gameName());
                ctx.result(gson.toJson(gameID));
            }
            catch (BadRequestException ex) {
                ctx.status(400);
                ctx.result(ex.getMessage());
            }
            catch (UnauthorizedException ex) {
                ctx.status(401);
                ctx.result(ex.getMessage());
            }
            catch (Exception ex) {
                ctx.status(500);
                ctx.result(ex.getMessage());
            }
        });

        // Join a game
        javalin.put("/game", ctx -> {
            try {
                String authToken = ctx.header("Authorization");
                NewPlayer newPlayer = gson.fromJson(ctx.body(), NewPlayer.class);
                gameService.joinGame(authToken, newPlayer.playerColor(), newPlayer.gameID());
                ctx.status(200);
            }
            catch (BadRequestException ex) {
                ctx.status(400);
                ctx.result(ex.getMessage());
            }
            catch (UnauthorizedException ex) {
                ctx.status(401);
                ctx.result(ex.getMessage());
            }
            catch (AlreadyTakenException ex) {
                ctx.status(403);
                ctx.result(ex.getMessage());
            }
            catch (Exception ex) {
                ctx.status(500);
                ctx.result(ex.getMessage());
            }
        });

        // clear application
        javalin.delete("/db", ctx -> {
            try {
                deleteService.delete();
                ctx.status(200);
            }
            catch (Exception ex) {
                ctx.status(500);
                ctx.result(ex.getMessage());
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
