package service;

import dataaccess.*;

import model.GameData;
import model.UserData;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService gameService;
    private AuthDAO authDAO;
    private String authToken;

    @BeforeEach
    public void setup() throws Exception {

        // Setup a valid user with a valid token as well as a valid database.
        UserDAO userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        UserData userData = new UserData("bob", "password", "bob@email.com");
        AuthData authData = userService.register(userData);
        authToken = authData.authToken();


    }
}