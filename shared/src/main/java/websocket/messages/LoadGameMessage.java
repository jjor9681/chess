// This is for adding a field "game".
// It was in the spec, but it wasn't in petshop.


package websocket.messages;
import chess.ChessGame;
import java.util.Objects;

public class LoadGameMessage extends ServerMessage {

    private final ChessGame game;

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        if (!(o instanceof LoadGameMessage that)) {
            return false;
        }

        return Objects.equals(
                getGame(),
                that.getGame());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                getGame());
    }
}