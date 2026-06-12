package dataaccess;

import chess.ChessGame;
import dataaccess.sqldataaccess.MySqlGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class MySqlGameDAOTest {

    private GameDAO gameDAO;

    @BeforeEach
    public void setup() throws Exception {
        DatabaseInitializer.initialize();
        gameDAO = new MySqlGameDAO();
        gameDAO.clear();
    }

    @Test
    public void createGamePositive() throws Exception {
        GameData game = new GameData(
                1,
                null,
                null,
                "200_EloGame",
                new ChessGame());
        gameDAO.createGame(game);
        GameData result = gameDAO.getGame(1);
        assertNotNull(result);
        assertEquals("200_EloGame", result.gameName());
    }

    @Test
    public void createGameNegative() throws Exception {
        GameData game = new GameData(
                1,
                null,
                null,
                "200_EloGame",
                new ChessGame());
        gameDAO.createGame(game);
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(game));
    }

    @Test
    public void getGamePositive() throws Exception {
        GameData game = new GameData(
                1,
                null,
                null,
                "200_EloGame",
                new ChessGame());

        gameDAO.createGame(game);
        GameData result = gameDAO.getGame(1);
        assertNotNull(result);
        assertEquals(1, result.gameID());
        assertEquals("200_EloGame", result.gameName());
        assertNotNull(result.game());
    }

    @Test
    public void getGameNegative() throws Exception {
        assertNull(gameDAO.getGame(999));
    }

    @Test
    public void listGamesPositive() throws Exception {
        gameDAO.createGame(new GameData(
                1,
                null,
                null,
                "3000ELO_GAME",
                new ChessGame()));

        gameDAO.createGame(new GameData(
                2,
                null,
                null,
                "200ELO_GAME",
                new ChessGame()));

        Collection<GameData> games = gameDAO.listGames();
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesNegative() throws Exception {
        Collection<GameData> games = gameDAO.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    public void updateGamePositive() throws Exception {
        GameData game = new GameData(
                1,
                null,
                null,
                "200_EloGame",
                new ChessGame());

        gameDAO.createGame(game);
        GameData updatedGame = new GameData(
                1,
                "Jonas",
                null,
                "200_EloGame",
                game.game());
        gameDAO.updateGame(updatedGame);
        GameData result = gameDAO.getGame(1);
        assertEquals("Jonas", result.whiteUsername());
    }

    @Test
    public void updateGameNegative() {
        GameData fakeGame = new GameData(
                999,
                "Jonas",
                null,
                "FakeGame",
                new ChessGame());

        assertDoesNotThrow(() -> gameDAO.updateGame(fakeGame));
    }

    @Test
    public void clearPositive() throws Exception {
        GameData game = new GameData(
                1,
                null,
                null,
                "200_EloGame",
                new ChessGame());

        gameDAO.createGame(game);
        gameDAO.clear();
        assertNull(gameDAO.getGame(1));
    }
}