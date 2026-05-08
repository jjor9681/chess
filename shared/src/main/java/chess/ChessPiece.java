package chess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        List<ChessMove> validMoves = new ArrayList<>(); // According to stack overflow, lists are made like this.
        if (piece.getPieceType() == PieceType.BISHOP){
            // return List.of(new ChessMove(new ChessPosition(5,4), new ChessPosition(1,8),null));
            // The expected output looks like it scans the board from left to right, moving up a column after each scan
            // So I should probably use nested for loops to do this.
            for (int c = 1; c <= 8 ; c++){
                if (c == myPosition.getColumn()){
                    // Do nothing. Can't move on the same column.
                    continue;
                }
                for (int r = 1; r <= 8; r++){
                    if (r == myPosition.getRow()){
                        // Do nothing. Can't move on the same row.
                        continue;
                    }
                    /* Now I need to know whether the bishop is on the same diagonal as
                    *  whatever square we are looking at, and I'm going to have to do it using math.
                    *  From ECEn 320's chess project, I remember that you can just measure the row difference
                    *  and column difference of the possible move from the piece's current position, and if
                    *  they're equal, then I'm chilling. And lengths are always positive so I can just abs.*/
                    if (Math.abs(r - myPosition.getRow()) == Math.abs(c - myPosition.getColumn())){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                    }
                }
            }
            return validMoves;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
