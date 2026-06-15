// Every command needs a consistent way to send errors.
// that is all handled here.

package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;

public class ErrorSender {

    private final ConnectionManagerPlus connectionManager;

    public ErrorSender(ConnectionManagerPlus connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void send(
            Session session,
            String message) {
        try {
            connectionManager.sendToOne(
                    session,
                    new ErrorMessage(message));
        } catch (Exception ignored) {
        }
    }
}