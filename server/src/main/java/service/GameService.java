package service;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import java.util.Collection;


public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    // phase 4 edit
    // I need to know how many games are currently in the server so that I can start
    // assigning ID's that won't overwrite any other games.
    private int trueGameID()
            throws DataAccessException {
        int highestID = 0;
        for (GameData game : gameDAO.listGames()) {
            highestID = Math.max(
                    highestID,
                    game.gameID());
        }
        return highestID + 1;
    }

    public GameService(GameDAO gameDataAccessObject, AuthDAO authDataAccessObject) {
        this.gameDAO = gameDataAccessObject;
        this.authDAO = authDataAccessObject;
    }

    public Collection<GameData> listGames(String authToken) throws UnauthorizedException, DataAccessException {

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }

    public int createGame(String authToken, String gameName) throws UnauthorizedException, BadRequestException, DataAccessException {

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        // Make sure the game exists.
        if (gameName == null) {
            throw new BadRequestException("Error: bad request");
        }

        ChessGame chessGame = new ChessGame();

        // phase 4 edit
        int gameID = trueGameID();


        GameData gameData = new GameData(gameID, null,null,gameName,chessGame);
        gameDAO.createGame(gameData);

        return gameID;
    }

    public void joinGame(String authToken,
                         String playerColor,
                         int gameID
                )   throws UnauthorizedException,
                    BadRequestException,
                    AlreadyTakenException,
                    DataAccessException {

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        // Time to retrieve some gameData using the gameID. If there is no game associated with the ID, then i'll throw.
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new BadRequestException("Error: bad request");
        }

        // Fixed error where I originally thought that null was okay to have and it meant spectator mode.
        if (playerColor == null || (!playerColor.equals("WHITE") && !playerColor.equals("BLACK"))) {

            throw new BadRequestException("Error: bad request");
        }

        // Time to make sure that someone else isn't already in the game when a new user tries to take their spot.
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();

        if ("WHITE".equals(playerColor)) {
            if (whiteUsername != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            whiteUsername = authData.username();
        } else {
            if (blackUsername != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            blackUsername = authData.username();
        }

        GameData newPlayerGame = new GameData(gameID, whiteUsername, blackUsername, gameData.gameName(), gameData.game());
        gameDAO.updateGame(newPlayerGame);
    }
}