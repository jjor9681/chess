// This file appears in the spec, so I will start here.
// I think all this is doing is giving MAKE_MOVE
// an extra field for ChessMove.

package websocket.commands;
import chess.ChessMove;
import java.util.Objects;

public class MakeMoveCommand extends UserGameCommand {

    private final ChessMove move;

    public MakeMoveCommand(
            String authToken,
            Integer gameID,
            ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }
    public ChessMove getMove() {
        return move;
    }
    @Override
    public boolean equals(Object object) {
        if (!super.equals(object)) {
            return false;
        }
        if (!(object instanceof MakeMoveCommand that)) {
            return false;
        }
        return Objects.equals(
                getMove(),
                that.getMove());
    }
    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                getMove());
    }
}