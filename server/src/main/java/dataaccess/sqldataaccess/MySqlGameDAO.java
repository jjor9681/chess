package dataaccess.sqldataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManagerWrapper;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
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
                    "Error: Can't make a new game!",
                    ex);
        }
    }

    @Override
    public GameData getGame(int gameID)
            throws DataAccessException {
        String statement =
                """
                SELECT gameID,
                       whiteUsername,
                       blackUsername,
                       gameName,
                       gameState
                FROM game
                WHERE gameID = ?
                """;
        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChessGame game =
                            new Gson().fromJson(
                                    rs.getString("gameState"),
                                    ChessGame.class);
                    return new GameData(
                            rs.getInt("gameID"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            rs.getString("gameName"),
                            game);
                }
            }
            return null;
        } catch (Exception ex) {
            throw new DataAccessException(
                    "Error: Unable to retrieve game",
                    ex);
        }
    }

    @Override
    public Collection<GameData> listGames()
            throws DataAccessException {
        String statement =
                """
                SELECT gameID,
                       whiteUsername,
                       blackUsername,
                       gameName,
                       gameState
                FROM game
                """;
        // haven't touched arraylist in a while. It's good to be back!
        Collection<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                ChessGame game =
                        new Gson().fromJson(
                                rs.getString("gameState"),
                                ChessGame.class);
                games.add(
                        new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                game));
            }
            return games;
        } catch (Exception ex) {
            throw new DataAccessException(
                    "Error: Unable to list games",
                    ex);
        }
    }

    @Override
    public void updateGame(GameData game)
            throws DataAccessException {
        String statement =
                """
                UPDATE game
                SET whiteUsername = ?,
                    blackUsername = ?,
                    gameName = ?,
                    gameState = ?
                WHERE gameID = ?
                """;
        String gameState =
                new Gson().toJson(game.game());
        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(
                    1,
                    game.whiteUsername());
            ps.setString(
                    2,
                    game.blackUsername());
            ps.setString(
                    3,
                    game.gameName());
            ps.setString(
                    4,
                    gameState);
            ps.setInt(
                    5,
                    game.gameID());
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException(
                    "Error: Unable to update game",
                    ex);
        }
    }

    @Override
    public void clear() throws DataAccessException {

        String statement = "TRUNCATE TABLE game";

        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.executeUpdate();

        } catch (Exception ex) {

            throw new DataAccessException(
                    "Error: couldn't clear the game table",
                    ex);
        }
    }
}