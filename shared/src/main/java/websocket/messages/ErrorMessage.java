// This is another phase 6 spec suggested file.
// The field ErrorMessage is what I'm really looking for though.

package websocket.messages;
import java.util.Objects;
public class ErrorMessage extends ServerMessage {

    private final String errorMessage;

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        if (!(o instanceof ErrorMessage that)) {
            return false;
        }

        return Objects.equals(
                getErrorMessage(),
                that.getErrorMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                getErrorMessage());
    }
}