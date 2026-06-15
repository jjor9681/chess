// actually apply a resignation.
// set's game over to true.
// save updated GameData.

package server.websocket.resign;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;

public class ResignMaker {

    private final GameDAO gameDAO;

    public ResignMaker(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void resign(GameData gameData) throws Exception {

        ChessGame game =
                gameData.game();

        game.setGameOver(true);

        GameData updatedGame =
                new GameData(
                        gameData.gameID(),
                        gameData.whiteUsername(),
                        gameData.blackUsername(),
                        gameData.gameName(),
                        game);

        gameDAO.updateGame(updatedGame);
    }
}