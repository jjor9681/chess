// If white leaves, white username is null
// likewise with black
// observers can leave whenever they want with no updates needed.
// If a player slot changed, then the game needs to be updated.

package server.websocket.leave;

import dataaccess.GameDAO;
import model.GameData;

public class LeaveMaker {

    private final GameDAO gameDAO;

    public LeaveMaker(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void leave(
            GameData gameData,
            String username) throws Exception {
        String whiteUsername =
                gameData.whiteUsername();
        String blackUsername =
                gameData.blackUsername();
        boolean playerLeft =
                false;
        if (username.equals(whiteUsername)) {
            whiteUsername = null;
            playerLeft = true;
        }
        if (username.equals(blackUsername)) {
            blackUsername = null;
            playerLeft = true;
        }
        if (!playerLeft) {
            return;
        }
        GameData updatedGame =
                new GameData(
                        gameData.gameID(),
                        whiteUsername,
                        blackUsername,
                        gameData.gameName(),
                        gameData.game());
        gameDAO.updateGame(updatedGame);
    }
}