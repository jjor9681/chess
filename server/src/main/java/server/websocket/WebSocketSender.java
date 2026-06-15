// All communication.

package server.websocket;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import java.io.IOException;
import java.util.Set;

public class WebSocketSender {

    private final Gson gson = new Gson();

    public void sendToOne(
            Session session,
            ServerMessage message) throws IOException {

        if (session != null && session.isOpen()) {
            session.getRemote().sendString(
                    gson.toJson(message));
        }
    }

    public void sendToMany(
            Set<Session> sessions,
            ServerMessage message) throws IOException {
        String jsonMessage =
                gson.toJson(message);

        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getRemote().sendString(
                        jsonMessage);
            }
        }
    }

    public void sendToManyExcept(
            Set<Session> sessions,
            Session excludedSession,
            ServerMessage message) throws IOException {
        String jsonMessage =
                gson.toJson(message);
        for (Session session : sessions) {
            if (session.isOpen()
                    && !session.equals(excludedSession)) {
                session.getRemote().sendString(
                        jsonMessage);
            }
        }
    }
}