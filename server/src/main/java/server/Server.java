package server;
import dataaccess.*;
import dataaccess.sqldataaccess.MySqlAuthDAO;
import dataaccess.sqldataaccess.MySqlGameDAO;
import dataaccess.sqldataaccess.MySqlUserDAO;
import handling.*;
import io.javalin.Javalin;
import service.DeleteService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    // DAOs
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    // Services
    private final UserService userService;
    private final GameService gameService;
    private final DeleteService deleteService;
    // Handlers
    private final Register register;
    private final Login login;
    private final Logout logout;
    private final ListGames listGames;
    private final CreateGame createGame;
    private final JoinGame joinGame;
    private final Clear clear;

    public Server() {


        // Phase 4 new addition. Everytime the server starts up,
        // there should always be a database to read from, so this
        // will create one if there is not.
        try {
            DatabaseInitializer.initialize();
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }

        javalin = Javalin.create(
                config -> config.staticFiles.add("web")
        );

        // DAO Initialization
        userDAO = new MySqlUserDAO();
        authDAO = new MySqlAuthDAO();
        gameDAO = new MySqlGameDAO();

        // Service Initialization
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        deleteService = new DeleteService(userDAO, authDAO, gameDAO);


        // Handleing Initialization
        register = new Register(userService);
        login = new Login(userService);
        logout = new Logout(userService);
        listGames = new ListGames(gameService);
        createGame = new CreateGame(gameService);
        joinGame = new JoinGame(gameService);
        clear = new Clear(deleteService);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);

        // endpoints
        javalin.delete("/db", clear);
        javalin.post("/user", register);
        javalin.post("/session", login);
        javalin.delete("/session", logout);
        javalin.get("/game", listGames);
        javalin.post("/game", createGame);
        javalin.put("/game", joinGame);

        return javalin.port();
    }
    public void stop() { // They had this in petshop as well as the startercode so I imagine it is needed.
        javalin.stop();
    }
}