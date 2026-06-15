// Almost a duplicate of petshop, but
// handles CONNECT, MAKE_MOVE, LEAVE, and RESIGN.
// Also receives ServerMessage objects.
// Forwards messages to NotificationHandler.

package client.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    private Session session;
    private final NotificationHandler notificationHandler;
    private final Gson gson = new Gson();

    public WebSocketFacade(
            String serverUrl,
            NotificationHandler notificationHandler) throws Exception {

        try {
            serverUrl =
                    serverUrl.replace(
                            "http",
                            "ws");
            URI socketURI =
                    new URI(
                            serverUrl + "/ws");
            this.notificationHandler =
                    notificationHandler;
            WebSocketContainer container =
                    ContainerProvider.getWebSocketContainer();
            this.session =
                    container.connectToServer(
                            this,
                            socketURI);
            this.session.addMessageHandler(
                    new MessageHandler.Whole<String>() {
                        @Override
                        public void onMessage(String message) {
                            ServerMessage serverMessage =
                                    gson.fromJson(
                                            message,
                                            ServerMessage.class);

                            notificationHandler.notify(
                                    serverMessage);
                        }
                    });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(
                    "Error: " + ex.getMessage());
        }
    }
    @Override
    public void onOpen(
            Session session,
            EndpointConfig endpointConfig) {
    }

    public void connect(
            String authToken,
            int gameID) throws Exception {

        send(
                new UserGameCommand(
                        UserGameCommand.CommandType.CONNECT,
                        authToken,
                        gameID));
    }

    public void makeMove(
            String authToken,
            int gameID,
            ChessMove move) throws Exception {

        send(
                new MakeMoveCommand(
                        authToken,
                        gameID,
                        move));
    }

    public void leave(
            String authToken,
            int gameID) throws Exception {

        send(
                new UserGameCommand(
                        UserGameCommand.CommandType.LEAVE,
                        authToken,
                        gameID));
    }

    public void resign(
            String authToken,
            int gameID) throws Exception {

        send(
                new UserGameCommand(
                        UserGameCommand.CommandType.RESIGN,
                        authToken,
                        gameID));
    }

    private void send(
            UserGameCommand command) throws Exception {

        try {
            session.getBasicRemote()
                    .sendText(
                            gson.toJson(command));
        } catch (IOException ex) {
            throw new Exception(
                    "Error: " + ex.getMessage());
        }
    }
}