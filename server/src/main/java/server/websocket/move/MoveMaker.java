// spec says a legal move needs to update the game and save it.
// obv gonna require a MySql update.

package server.websocket.move;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;
import websocket.commands.MakeMoveCommand;

public class MoveMaker {

    private final GameDAO gameDAO;

    public MoveMaker(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public ChessGame makeMove(
            MakeMoveCommand command,
            GameData gameData) throws Exception {

        ChessGame game =
                gameData.game();

        game.makeMove(
                command.getMove());

        GameData updatedGame =
                new GameData(
                        gameData.gameID(),
                        gameData.whiteUsername(),
                        gameData.blackUsername(),
                        gameData.gameName(),
                        game);

        gameDAO.updateGame(updatedGame);

        return game;
    }
}