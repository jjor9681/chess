package server.websocket.resign;

import model.AuthData;
import server.websocket.ConnectionManagerPlus;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

public class ResignCommunicator {

    private final ConnectionManagerPlus connectionManager;

    public ResignCommunicator(
            ConnectionManagerPlus connectionManager) {

        this.connectionManager = connectionManager;
    }

    public void sendResignMessage(
            UserGameCommand command,
            AuthData authData) throws Exception {

        connectionManager.broadcastToGame(
                command.getGameID(),
                new NotificationMessage(
                        authData.username()
                                + " resigned.")); // feels like i'm playing age of empires
    }
}