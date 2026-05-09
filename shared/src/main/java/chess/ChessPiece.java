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
            // Okay my previous strategy sucked at detected other pieces so I am trying something else...

            // for loop that point to all four corners away from the bishop.
            for (int i = 0; i < 4; i++) {
                int rowPath = 0;
                int colPath = 0;
                switch (i) {

                    // North East of bishop
                    case 0:
                        rowPath = 1;
                        colPath = 1;
                        break;
                    // South East of bishop
                    case 1:
                        rowPath = -1;
                        colPath = 1;
                        break;
                    // South West of bishop
                    case 2:
                        rowPath = -1;
                        colPath = -1;
                        break;
                    // North West of bishop
                    case 3:
                        rowPath = 1;
                        colPath = -1;
                        break;
                }

                int r = myPosition.getRow() + rowPath;
                int c = myPosition.getColumn() + colPath;

                // Makes sure we are not targeting out of bounds. When we do target out of bounds, or
                // we discover a friendly/enemy, break.
                while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {

                    // Make a new object real quick for where we are looking and if there is a piece there,
                    // we are going to target it.
                    ChessPosition pos = new ChessPosition(r, c);
                    ChessPiece target = board.getPiece(pos);

                    // If nobody is there, then we are good to go.
                    if (target == null) {
                        validMoves.add(new ChessMove(myPosition, pos, null));
                    }
                    // If there is someone there, i just gotta check if their color isn't the same.
                    else {

                        if (target.getTeamColor() != piece.getTeamColor()) {
                            validMoves.add(new ChessMove(myPosition, pos, null));
                        }
                        break;
                    }
                    // Update target depending on the direction we are looking.
                    r += rowPath;
                    c += colPath;
                }
            }
            return validMoves;
        }
        if (piece.getPieceType() == PieceType.KING){
            // For the king, I am thinking of a for loop that will iterate all 8 spaces around it.
            // I can do a switch statement again for all 8 slots.
            // Then the team color check.
            for (int i = 0; i < 8; i++) {

                // Some helper variables.
                int rowChange = 0;
                int colChange = 0;

                switch (i) {

                    case 0: // bottom-left
                        rowChange = -1;
                        colChange = -1;
                        break;

                    case 1: // bottom
                        rowChange = -1;
                        colChange = 0;
                        break;

                    case 2: // bottom-right
                        rowChange = -1;
                        colChange = 1;
                        break;

                    case 3: // left
                        rowChange = 0;
                        colChange = -1;
                        break;

                    case 4: // right
                        rowChange = 0;
                        colChange = 1;
                        break;

                    case 5: // top-left
                        rowChange = 1;
                        colChange = -1;
                        break;

                    case 6: // top
                        rowChange = 1;
                        colChange = 0;
                        break;

                    case 7: // top-right
                        rowChange = 1;
                        colChange = 1;
                        break;
                }

                int r = myPosition.getRow() + rowChange;
                int c = myPosition.getColumn() + colChange;

                // Is the target in bounds?
                if (r < 1 || r > 8 || c < 1 || c > 8) {
                    continue;
                }

                // Time to check if the target at (r,c) is valid.
                // Now I'll just do the same thing as with the bishop.
                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece target = board.getPiece(pos);

                // If nobody is there, then we are good to go.
                if (target == null) {
                    validMoves.add(new ChessMove(myPosition, pos, null));
                }
                // If there is someone there, i just gotta check if their color isn't the same.
                else {

                    if (target.getTeamColor() != piece.getTeamColor()) {
                        validMoves.add(new ChessMove(myPosition, pos, null));
                    }
                }

            }
            return validMoves;
        }
        if (piece.getPieceType() == PieceType.KNIGHT){
            // This is just going to be a copy paste of the king but with different switch statements.
            for (int i = 0; i < 8; i++) {

                // helpful variables again
                int rowChange = 0;
                int colChange = 0;

                switch (i) {

                    case 0:
                        rowChange = -2;
                        colChange = -1;
                        break;

                    case 1:
                        rowChange = -2;
                        colChange = 1;
                        break;

                    case 2:
                        rowChange = -1;
                        colChange = -2;
                        break;

                    case 3:
                        rowChange = -1;
                        colChange = 2;
                        break;

                    case 4:
                        rowChange = 1;
                        colChange = -2;
                        break;

                    case 5:
                        rowChange = 1;
                        colChange = 2;
                        break;

                    case 6:
                        rowChange = 2;
                        colChange = -1;
                        break;

                    case 7:
                        rowChange = 2;
                        colChange = 1;
                        break;
                }

                int r = myPosition.getRow() + rowChange;
                int c = myPosition.getColumn() + colChange;

                if (r < 1 || r > 8 || c < 1 || c > 8) {
                    continue;
                }

                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece target = board.getPiece(pos);

                if (target == null) {
                    validMoves.add(new ChessMove(myPosition, pos, null));
                }
                else {
                    if (target.getTeamColor() != piece.getTeamColor()) {
                        validMoves.add(new ChessMove(myPosition, pos, null));
                    }
                }
            }

            return validMoves;
        }
        if (piece.getPieceType() == PieceType.PAWN){

        }
        if (piece.getPieceType() == PieceType.QUEEN){
            // High key this is just a combination of bishop then rook.

            for (int i = 0; i < 8; i++) {

                int rowChange = 0;
                int colChange = 0;

                switch (i) {

                    // diagonals first (copied from bishop.)

                    case 0: // top-right
                        rowChange = 1;
                        colChange = 1;
                        break;

                    case 1: // bottom-right
                        rowChange = -1;
                        colChange = 1;
                        break;

                    case 2: // bottom-left
                        rowChange = -1;
                        colChange = -1;
                        break;

                    case 3: // top-left
                        rowChange = 1;
                        colChange = -1;
                        break;

                    // horizontals/verticals second (Copied from rook)

                    case 4: // left
                        rowChange = 0;
                        colChange = -1;
                        break;

                    case 5: // right
                        rowChange = 0;
                        colChange = 1;
                        break;

                    case 6: // down
                        rowChange = -1;
                        colChange = 0;
                        break;

                    case 7: // up
                        rowChange = 1;
                        colChange = 0;
                        break;
                }

                int r = myPosition.getRow() + rowChange;
                int c = myPosition.getColumn() + colChange;

                while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {

                    ChessPosition pos = new ChessPosition(r, c);
                    ChessPiece target = board.getPiece(pos);

                    if (target == null) {
                        validMoves.add(new ChessMove(myPosition, pos, null));
                    }
                    else {

                        if (target.getTeamColor() != piece.getTeamColor()) {
                            validMoves.add(new ChessMove(myPosition, pos, null));
                        }

                        break;
                    }

                    r += rowChange;
                    c += colChange;
                }
            }
            return validMoves;
        }
        if (piece.getPieceType() == PieceType.ROOK){
            // Should be the same thing as bishop, except one of the change variables needs to always be zero.
            for (int i = 0; i < 4; i++) {

                int rowChange = 0;
                int colChange = 0;

                switch (i) {

                    case 0: // left
                        rowChange = 0;
                        colChange = -1;
                        break;

                    case 1: // right
                        rowChange = 0;
                        colChange = 1;
                        break;

                    case 2: // down
                        rowChange = -1;
                        colChange = 0;
                        break;

                    case 3: // up
                        rowChange = 1;
                        colChange = 0;
                        break;
                }

                int r = myPosition.getRow() + rowChange;
                int c = myPosition.getColumn() + colChange;

                while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {

                    ChessPosition pos = new ChessPosition(r, c);
                    ChessPiece target = board.getPiece(pos);

                    if (target == null) {
                        validMoves.add(new ChessMove(myPosition, pos, null));
                    }
                    else {

                        if (target.getTeamColor() != piece.getTeamColor()) {
                            validMoves.add(new ChessMove(myPosition, pos, null));
                        }
                        break;
                    }

                    r += rowChange;
                    c += colChange;
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
