// The spec says I only need a positive test here.
package service;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteServiceTest {
    private DeleteService deleteService;
    private UserService userService;
    private GameService gameService;
    private String authToken;

    @BeforeEach
    public void setup() throws Exception {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        deleteService = new DeleteService(userDAO, authDAO, gameDAO);
        UserData userData = new UserData("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        authToken = userService.register(userData).authToken();
        gameService.createGame(authToken, "200_EloGame");
    }

    @Test
    public void deletePositive() throws Exception {
        deleteService.delete();
        assertThrows(UnauthorizedException.class, () -> gameService.listGames(authToken));
    }
}