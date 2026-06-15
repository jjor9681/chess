// I need message and getMessage() so.. ta-da!
// This was in the phase 6 spec and also in petshop.

package websocket.messages;

import java.util.Objects;

public class NotificationMessage extends ServerMessage {

    private final String message;

    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        if (!(o instanceof NotificationMessage that)) {
            return false;
        }

        return Objects.equals(
                getMessage(),
                that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                getMessage());
    }
}