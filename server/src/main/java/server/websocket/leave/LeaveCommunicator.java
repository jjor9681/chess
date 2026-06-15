// notify everybody else
// and remove the websocket connection for the player who left

package server.websocket.leave;

import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ConnectionManagerPlus;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

public class LeaveCommunicator {

    private final ConnectionManagerPlus connectionManager;

    public LeaveCommunicator(
            ConnectionManagerPlus connectionManager) {

        this.connectionManager = connectionManager;
    }
    public void sendLeaveMessage(
            UserGameCommand command,
            Session session,
            AuthData authData) throws Exception {
        connectionManager.broadcastToGameExcept(
                command.getGameID(),
                session,
                new NotificationMessage(
                        authData.username()
                                + " left the game."));
        connectionManager.remove(
                command.getGameID(),
                session);
    }
}