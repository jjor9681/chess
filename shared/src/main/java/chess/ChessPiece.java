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

    // To help my code pass the coding check,
    // I can use the same strategy we used in ECEn 340 for the piece movements.
    private static final int[][] bishopDiagonal = {
            {1, 1},
            {-1, 1},
            {-1, -1},
            {1, -1}
    };
    private static final int[][] rookHorizontalPlusVertical = {
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0}
    };
    private static final int[][] queenBishopRookCombo = {
            {1, 1},
            {-1, 1},
            {-1, -1},
            {1, -1},
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0}
    };
    private static final int[][] kingMove = {
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 0},
            {1, 1}
    };
    private static final int[][] knightMove = {
            {-2, -1},
            {-2, 1},
            {-1, -2},
            {-1, 2},
            {1, -2},
            {1, 2},
            {2, -1},
            {2, 1}
    };
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

    // This method is for reducing the total code in the file by reusing the same code for multiple pieces.
    private void bishopRookQueenMoves(
            ChessBoard board,
            ChessPosition myPosition,
            List<ChessMove> validMoves,
            int[][] directions
    ) {
        for (int[] direction : directions) {
            int rowChange = direction[0];
            int colChange = direction[1];
            int r = myPosition.getRow() + rowChange;
            int c = myPosition.getColumn() + colChange;
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece target = board.getPiece(pos);
                if (target == null) {
                    validMoves.add(new ChessMove(myPosition, pos, null));
                }
                else {
                    if (target.getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, pos, null));
                    }
                    break;
                }
                r += rowChange;
                c += colChange;
            }
        }
    }
    private void kingKnightMoves(
            ChessBoard board,
            ChessPosition myPosition,
            List<ChessMove> validMoves,
            int[][] moves
    ) {
        for (int[] move : moves) {
            int r = myPosition.getRow() + move[0];
            int c = myPosition.getColumn() + move[1];
            if (r < 1 || r > 8 || c < 1 || c > 8) {
                continue;
            }
            ChessPosition pos = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(pos);
            if (target == null ||
                    target.getTeamColor() != pieceColor) {
                validMoves.add(new ChessMove(myPosition, pos, null));
            }
        }
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
        if (type == PieceType.BISHOP) {
            bishopRookQueenMoves(board, myPosition, validMoves, bishopDiagonal);
            return validMoves;
        }

        if (type == PieceType.ROOK) {
            bishopRookQueenMoves(board, myPosition, validMoves, rookHorizontalPlusVertical);
            return validMoves;
        }

        if (type == PieceType.QUEEN) {
            bishopRookQueenMoves(board, myPosition, validMoves, queenBishopRookCombo);
            return validMoves;
        }

        if (type == PieceType.KING) {
            kingKnightMoves(board, myPosition, validMoves, kingMove);
            return validMoves;
        }

        if (type == PieceType.KNIGHT) {
            kingKnightMoves(board, myPosition, validMoves, knightMove);
            return validMoves;
        }

        if (piece.getPieceType() == PieceType.PAWN){
            // Pawn is significantly more complicated. I'll use an if statement to separate team white and team black.
            // White pawns advance to the north. The north square needs to be empty, and the northeast/northwest
            // squares need to have an enemy. Then check if the white piece is on row 2. Then do the same thing but
            // facing the south and checking row 7 for the double move.

            // White pawn
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {

                // Check square in front of the pawn
                int r = myPosition.getRow() + 1;
                int c = myPosition.getColumn();

                ChessPosition pos = new ChessPosition(r, c);

                // pawn can't capture forward. If something is in the way, too bad!
                if (board.getPiece(pos) == null) {

                    // Check for promo
                    if (r == 8) {

                        validMoves.add(new ChessMove(myPosition, pos, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, pos, PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, pos, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, pos, PieceType.KNIGHT));
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, pos, null));
                    }

                    // double move
                    if (myPosition.getRow() == 2) {

                        ChessPosition doublePos = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());

                        // Both spaces must be empty.
                        if (board.getPiece(doublePos) == null) {
                            // Check for promo
                            validMoves.add(new ChessMove(myPosition, doublePos, null));
                        }
                    }
                }

                // Check north west
                r = myPosition.getRow() + 1;
                c = myPosition.getColumn() - 1;
                if (c >= 1) {
                    pos = new ChessPosition(r, c);
                    ChessPiece target = board.getPiece(pos);
                    if (target != null &&
                            target.getTeamColor() != piece.getTeamColor()) {

                        // Check for promo
                        if (r == 8) {

                            validMoves.add(new ChessMove(myPosition, pos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Check North East
                r = myPosition.getRow() + 1;
                c = myPosition.getColumn() + 1;

                if (c <= 8) {

                    pos = new ChessPosition(r, c);
                    ChessPiece target = board.getPiece(pos);

                    if (target != null &&
                            target.getTeamColor() != piece.getTeamColor()) {

                        // Check for promo
                        if (r == 8) {

                            validMoves.add(new ChessMove(myPosition, pos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }
            }

            // Black pawn, basically the same as the white but towards the south.
            else {

                // Check forward one square.
                int r = myPosition.getRow() - 1;
                int c = myPosition.getColumn();

                ChessPosition pos = new ChessPosition(r, c);

                if (board.getPiece(pos) == null) {

                    // Check for promo
                    if (r == 1) {

                        validMoves.add(new ChessMove(myPosition, pos, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, pos, PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, pos, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, pos, PieceType.KNIGHT));
                    }
                    else {
                        validMoves.add(new ChessMove(myPosition, pos, null));
                    }

                    // double move.
                    if (myPosition.getRow() == 7) {

                        ChessPosition doublePos = new ChessPosition(myPosition.getRow() - 2,
                                        myPosition.getColumn());

                        if (board.getPiece(doublePos) == null) {
                            // Check for promo
                            validMoves.add(new ChessMove(myPosition, doublePos, null));
                        }
                    }
                }

                // Check south west capture.
                r = myPosition.getRow() - 1;
                c = myPosition.getColumn() - 1;

                if (c >= 1) {

                    pos = new ChessPosition(r, c);
                    ChessPiece target = board.getPiece(pos);

                    if (target != null &&
                            target.getTeamColor() != piece.getTeamColor()) {

                        // Check for promo
                        if (r == 1) {

                            validMoves.add(new ChessMove(myPosition, pos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Check south east capture.
                r = myPosition.getRow() - 1;
                c = myPosition.getColumn() + 1;

                if (c <= 8) {

                    pos = new ChessPosition(r, c);
                    ChessPiece target = board.getPiece(pos);

                    if (target != null &&
                            target.getTeamColor() != piece.getTeamColor()) {

                        // Check for promo
                        if (r == 1) {

                            validMoves.add(new ChessMove(myPosition, pos, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, pos, PieceType.KNIGHT));
                        }
                        else {
                            validMoves.add(new ChessMove(myPosition, pos, null));
                        }
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
