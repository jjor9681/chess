package dataaccess.sqldataaccess;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManagerWrapper;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class MySqlGameDAO implements GameDAO {

    @Override
    public void createGame(GameData game)
            throws DataAccessException {

        String statement =
                """
                INSERT INTO game
                (gameID, whiteUsername,
                 blackUsername, gameName,
                 gameState)
                VALUES (?, ?, ?, ?, ?)
                """;

        String gameState =
                // First time i've had to use some gson! Nice!
                new Gson().toJson(game.game());

        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setInt(
                    1,
                    game.gameID());
            ps.setString(
                    2,
                    game.whiteUsername());
            ps.setString(
                    3,
                    game.blackUsername());
            ps.setString(
                    4,
                    game.gameName());
            ps.setString(
                    5,
                    gameState);
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException(
                    "Unable to create game",
                    ex);
        }
    }

    @Override
    public GameData getGame(int gameID)
            throws DataAccessException {

        return null;
    }

    @Override
    public Collection<GameData> listGames()
            throws DataAccessException {

        return null;
    }

    @Override
    public void updateGame(GameData game)
            throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

        String statement = "TRUNCATE TABLE game";

        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.executeUpdate();

        } catch (Exception ex) {

            throw new DataAccessException(
                    "couldn't clear the game table",
                    ex);
        }
    }
}