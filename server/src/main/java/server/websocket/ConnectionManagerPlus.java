// The petshop connection manager handled "session".
// I am taking the idea and having it store Session, username, authToken, and gameID.

package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class ConnectionManagerPlus {

    private final GameIDMemory gameIDMemory =
            new GameIDMemory();

    private final WebSocketSender webSocketSender =
            new WebSocketSender();

    public void add(
            int gameID,
            Session session) {

        gameIDMemory.add(
                gameID,
                session);
    }

    public void remove(
            int gameID,
            Session session) {

        gameIDMemory.remove(
                gameID,
                session);
    }

    public void sendToOne(
            Session session,
            ServerMessage message) throws IOException {

        webSocketSender.sendToOne(
                session,
                message);
    }

    public void broadcastToGame(
            int gameID,
            ServerMessage message) throws IOException {

        webSocketSender.sendToMany(
                gameIDMemory.getConnections(gameID),
                message);
    }

    public void broadcastToGameExcept(
            int gameID,
            Session excludedSession,
            ServerMessage message) throws IOException {

        webSocketSender.sendToManyExcept(
                gameIDMemory.getConnections(gameID),
                excludedSession,
                message);
    }
}