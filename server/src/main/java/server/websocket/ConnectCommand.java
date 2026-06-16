package server.websocket;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class ConnectCommand {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final ConnectionManagerPlus connectionManager;
    private final ErrorSender errorSender;

    public ConnectCommand(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ConnectionManagerPlus connectionManager,
            ErrorSender errorSender) {

        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.connectionManager = connectionManager;
        this.errorSender = errorSender;
    }

    private String connectMessage(
            AuthData authData,
            GameData gameData) {

        String username =
                authData.username();

        if (username.equals(gameData.whiteUsername())) {
            return username + " joined as WHITE.";
        }

        if (username.equals(gameData.blackUsername())) {
            return username + " joined as BLACK.";
        }

        return username + " joined as an observer.";
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
            connectionManager.add(
                    command.getGameID(),
                    session);
            connectionManager.sendToOne(
                    session,
                    new LoadGameMessage(
                            gameData.game()));
            connectionManager.broadcastToGameExcept(
                    command.getGameID(),
                    session,
                    new NotificationMessage(
                            connectMessage(
                                    authData,
                                    gameData)));
        }
        catch (Exception ex) {
            errorSender.send(
                    session,
                    "Error: " + ex.getMessage());
        }
    }
}