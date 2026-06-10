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
    private static final int[][] BISHOP_DIAGONAL = {
            {1, 1},
            {-1, 1},
            {-1, -1},
            {1, -1}
    };
    private static final int[][] ROOK_HORIZONTAL_PLUS_VERTICAL = {
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0}
    };
    private static final int[][] QUEEN_BISHOP_ROOK_COMBO = {
            {1, 1},
            {-1, 1},
            {-1, -1},
            {1, -1},
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0}
    };
    private static final int[][] KING_MOVE = {
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 0},
            {1, 1}
    };
    private static final int[][] KNIGHT_MOVE = {
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
    // Avoiding duplicate code with pawn promotions
    private void addPromotionMoves(
            ChessPosition myPosition,
            ChessPosition pos,
            Collection<ChessMove> validMoves
    ) {
        validMoves.add(new ChessMove(myPosition, pos, PieceType.QUEEN));
        validMoves.add(new ChessMove(myPosition, pos, PieceType.ROOK));
        validMoves.add(new ChessMove(myPosition, pos, PieceType.BISHOP));
        validMoves.add(new ChessMove(myPosition, pos, PieceType.KNIGHT));
    }
    private void addPawnMove(
            ChessPosition myPosition,
            ChessPosition pos,
            Collection<ChessMove> validMoves,
            boolean promotion
    ) {
        if (promotion) {
            addPromotionMoves(myPosition, pos, validMoves);
            return;
        }
        validMoves.add(new ChessMove(myPosition, pos, null));
    }
    private void pawnMoves(
            ChessBoard board,
            ChessPosition myPosition,
            Collection<ChessMove> validMoves,
            int direction,
            int startRow,
            int promotionRow
    ) {
        int r = myPosition.getRow() + direction;
        int c = myPosition.getColumn();
        ChessPosition pos = new ChessPosition(r, c);
        // forward move
        if (board.getPiece(pos) == null) {
            addPawnMove(myPosition, pos, validMoves, r == promotionRow);
            // double move
            if (myPosition.getRow() == startRow) {
                ChessPosition doublePos =
                        new ChessPosition(
                                myPosition.getRow()
                                        + (2 * direction), // Could be negative, so I'll use multiplication
                                        c
                        );
                if (board.getPiece(doublePos) == null) {
                    validMoves.add(new ChessMove(myPosition, doublePos, null));
                }
            }
        }
        // capture left
        captureMove(
                board,
                myPosition,
                validMoves,
                direction,
                -1,
                promotionRow
        );
        // capture right
        captureMove(
                board,
                myPosition,
                validMoves,
                direction,
                1,
                promotionRow
        );
    }

    private void captureMove(
            ChessBoard board,
            ChessPosition myPosition,
            Collection<ChessMove> validMoves,
            int direction,
            int columnOffset,
            int promotionRow
    ) {
        int r = myPosition.getRow() + direction;
        int c = myPosition.getColumn() + columnOffset;
        if (c < 1 || c > 8) {
            return;
        }
        ChessPosition pos = new ChessPosition(r, c);
        ChessPiece target = board.getPiece(pos);
        if (target != null &&
                target.getTeamColor() != pieceColor) {
            addPawnMove(myPosition, pos, validMoves, r == promotionRow);
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
            bishopRookQueenMoves(board, myPosition, validMoves, BISHOP_DIAGONAL);
            return validMoves;
        }

        if (type == PieceType.ROOK) {
            bishopRookQueenMoves(board, myPosition, validMoves, ROOK_HORIZONTAL_PLUS_VERTICAL);
            return validMoves;
        }

        if (type == PieceType.QUEEN) {
            bishopRookQueenMoves(board, myPosition, validMoves, QUEEN_BISHOP_ROOK_COMBO);
            return validMoves;
        }

        if (type == PieceType.KING) {
            kingKnightMoves(board, myPosition, validMoves, KING_MOVE);
            return validMoves;
        }

        if (type == PieceType.KNIGHT) {
            kingKnightMoves(board, myPosition, validMoves, KNIGHT_MOVE);
            return validMoves;
        }

        if (type == PieceType.PAWN) {

            int direction;
            int startRow;
            int promotionRow;

            if (pieceColor == ChessGame.TeamColor.WHITE) {
                direction = 1;
                startRow = 2;
                promotionRow = 8;
            }
            else {
                direction = -1;
                startRow = 7;
                promotionRow = 1;
            }

            pawnMoves(
                    board,
                    myPosition,
                    validMoves,
                    direction,
                    startRow,
                    promotionRow
            );

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
