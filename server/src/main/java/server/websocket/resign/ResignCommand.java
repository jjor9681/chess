// should be complete.

package server.websocket.resign;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ConnectionManagerPlus;
import server.websocket.ErrorSender;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

public class ResignCommand {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final ConnectionManagerPlus connectionManager;
    private final ErrorSender errorSender;

    public ResignCommand(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ConnectionManagerPlus connectionManager,
            ErrorSender errorSender) {

        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.connectionManager = connectionManager;
        this.errorSender = errorSender;
    }

    public void execute(
            UserGameCommand command,
            Session session) {
        try {
            AuthData authData =
                    authDAO.getAuth(
                            command.getAuthToken());
            if (authData == null) {
                errorSender.send(
                        session,
                        "Error: unauthorized");
                return;
            }
            GameData gameData =
                    gameDAO.getGame(
                            command.getGameID());
            if (gameData == null) {
                errorSender.send(
                        session,
                        "Error: game not found");
                return;
            }

            if (!isPlayer(
                    authData.username(),
                    gameData)) {

                errorSender.send(
                        session,
                        "Error: observers cannot resign");
                return;
            }

            ChessGame game =
                    gameData.game();

            if (game.isGameOver()) {
                errorSender.send(
                        session,
                        "Error: game is already over");
                return;
            }

            game.setGameOver(true);

            GameData updatedGame =
                    new GameData(
                            gameData.gameID(),
                            gameData.whiteUsername(),
                            gameData.blackUsername(),
                            gameData.gameName(),
                            game);

            gameDAO.updateGame(updatedGame);

            connectionManager.broadcastToGame(
                    command.getGameID(),
                    new NotificationMessage(
                            authData.username()
                                    + " resigned."));

        } catch (Exception ex) {
            errorSender.send(
                    session,
                    "Error: " + ex.getMessage());
        }
    }

    private boolean isPlayer(
            String username,
            GameData gameData) {

        return username.equals(gameData.whiteUsername())
                || username.equals(gameData.blackUsername());
    }
}