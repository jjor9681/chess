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
        UserData userData = new UserData("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        AuthData authData = userService.register(userData);
        authToken = authData.authToken();
    }

    @Test
    public void listGamesPositive() throws Exception {
        gameService.createGame(authToken, "200_EloGame");
        Collection<GameData> games = gameService.listGames(authToken);
        assertEquals(1, games.size()); // As long as gamesTotal++ updates after the assignment, this should work.
    }
    @Test
    public void listGamesNegative() {
        assertThrows(UnauthorizedException.class, () -> gameService.listGames("SomebodyElse'sToken"));
    }

    @Test
    public void createGamePositive() throws Exception {
        int gameID = gameService.createGame(authToken, "200_EloGame");
        assertTrue(gameID > 0);
    }
    @Test
    public void createGameNegative() {
        assertThrows(BadRequestException.class, () -> gameService.createGame(authToken, null));
    }

    @Test
    public void joinGamePositive() throws Exception {
        int gameID = gameService.createGame(authToken, "200_EloGame");
        assertDoesNotThrow(() -> gameService.joinGame(authToken, "WHITE", gameID));
    }
    @Test
    public void joinGameNegative() throws Exception {
        int gameID = gameService.createGame(authToken, "200_EloGame");
        gameService.joinGame(authToken, "WHITE", gameID);
        assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(authToken, "WHITE", gameID));
    }
}