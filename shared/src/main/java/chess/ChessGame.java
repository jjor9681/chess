package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    // From what I'm learning, these private variables are called "fields" ...
    // They belong to the object itself. So every ChessGame() object that I make will always store
    // its own version of teamTurn and board.
    // The variables inside of ChessGame() are local variables and only exist temporarily inside the constructor.

    private TeamColor teamTurn;
    private ChessBoard board; // High key not sure if I'm going to ever need to change boards so I'll use final.
    // Sounds like using final prevents the board pointer from ever changing.

    // Okay for extra credit I need to remember when pieces have moved. In ECEn 320,
    // I just kept track of if the rooks/kings have ever moved. For en passant,
    // i just kept track of every move made ever, and if it was a pawn double move,
    // then adjacent enemy pawns get to attack.
    private boolean whiteKingCanCastle;
    private boolean blackKingCanCastle;
    private boolean whiteLeftRookCanCastle;
    private boolean whiteRightRookCanCastle;
    private boolean blackLeftRookCanCastle;
    private boolean blackRightRookCanCastle;
    private ChessMove lastMove;

    // Phase 6. I need a way to communicate that the game is over.
    private boolean gameOver;

    public ChessGame() {
        // White always goes first so I can just have it always start with white.
        teamTurn = TeamColor.WHITE;

        // I need to start thinking about how to make an actual chess game. I should start with the board.
        board = new ChessBoard();
        // And it needs all of its pieces.
        board.resetBoard();

        // Extra credit setup.
        whiteKingCanCastle = true;
        blackKingCanCastle = true;
        whiteLeftRookCanCastle = true;
        whiteRightRookCanCastle = true;
        blackLeftRookCanCastle = true;
        blackRightRookCanCastle = true;
        lastMove = null;

        gameOver = false;
    }

    // Phase 6 methods.

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        // I originally thought I should use parity to track whose team it is but
        // high key that would suck if i loaded in an arbitrary position and didn't
        // have a way to show how many moves were made already. All of this was before
        // I realized the method below setTeamTurn() can just be used at the end of every turn
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,// I was accessing this earlier with ChessGame.TeamColor.WHITE which makes sense.
        BLACK
    }

    private boolean teamHasLegalMoves(TeamColor teamColor) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition position = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(position);
                if (piece == null || piece.getTeamColor() != teamColor) {
                    continue;
                }
                Collection<ChessMove> moves = validMoves(position);
                if (!moves.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean kingIsSafe(ChessPosition start, ChessPosition end, ChessPiece movingPiece, ChessPiece capturedPiece) {
        board.addPiece(start, null);
        board.addPiece(end, movingPiece);
        boolean safe = !isInCheck(movingPiece.getTeamColor());
        board.addPiece(end, capturedPiece);
        board.addPiece(start, movingPiece);
        return safe;
    }

    private boolean castleKingSafe(ChessPosition startPosition, ChessPosition temporaryPosition, ChessPiece king) {
        board.addPiece(startPosition, null);
        board.addPiece(temporaryPosition, king);
        boolean safe = !isInCheck(king.getTeamColor());
        board.addPiece(temporaryPosition, null);
        board.addPiece(startPosition, king);
        return safe;
    }

    private void checkAndAddCastlingMoves(Collection<ChessMove> validMoves,
                                          ChessPiece piece,
                                          ChessPosition startPosition,
                                          int row, boolean leftCastle,
                                          boolean rightCastle) {
        if (leftCastle) {
            // are the in between squares all empty?
            ChessPosition leftTwo = new ChessPosition(row, 2);
            ChessPosition leftThree = new ChessPosition(row, 3);
            ChessPosition leftFour = new ChessPosition(row, 4);
            if (board.getPiece(leftTwo) == null && board.getPiece(leftThree) == null && board.getPiece(leftFour) == null) {
                // Row is empty. Now time to simulate checks.
                if (castleKingSafe(startPosition, leftFour, piece) && castleKingSafe(startPosition, leftThree, piece)) {
                    validMoves.add(new ChessMove(startPosition, leftThree, null));
                }
            }
        }
        if (rightCastle) {
            // are the in between squares all empty?
            ChessPosition rightSix = new ChessPosition(row, 6);
            ChessPosition rightSeven = new ChessPosition(row, 7);
            if (board.getPiece(rightSix) == null && board.getPiece(rightSeven) == null) {
                // Row is empty. Now time to simulate checks.
                if (castleKingSafe(startPosition, rightSix, piece) && castleKingSafe(startPosition, rightSeven, piece)) {
                    validMoves.add(new ChessMove(startPosition, rightSeven, null));
                }
            }
        }
    }

    private void executeCastleRookMove(int row, int oldCol, int newCol) {
        //Remove both the King and the Rook.
        ChessPosition oldRook = new ChessPosition(row, oldCol);
        ChessPiece rook = board.getPiece(oldRook);
        board.addPiece(oldRook, null);
        board.addPiece(new ChessPosition(row, 5), null);
        // Place new rook.
        board.addPiece(new ChessPosition(row, newCol), rook);
    }

    private void updateRookCastlingFlags(ChessPosition pos) {
        // If a rook has moved, it can no longer castle with the king.
        // Now I am worried about someone promoting to a rook, and then moving it to a square that can castle.
        // If a corner square is ever captured, then this can happen. So I need to update the fields when a corner Rook gets captured.
        if (pos.getRow() == 1 && pos.getColumn() == 1) {
            whiteLeftRookCanCastle = false;
        } else if (pos.getRow() == 1 && pos.getColumn() == 8) {
            whiteRightRookCanCastle = false;
        } else if (pos.getRow() == 8 && pos.getColumn() == 1) {
            blackLeftRookCanCastle = false;
        } else if (pos.getRow() == 8 && pos.getColumn() == 8) {
            blackRightRookCanCastle = false;
        }
    }

    private ChessPosition findKingPosition(TeamColor teamColor) {
        // How do I know where the King is? I don't know how to track him.
        // Do I store a variable with his current location?
        // Or should I just scan the board every time for him with nested loops?
        // Let's try that.
        // If I find him, i'll store his location in this variable.
        // Now the nested for loops.
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                // Okay let's get the piece at this location.
                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(pos);
                // Is there actually a piece there?
                if (piece == null) {
                    continue;
                }
                // Is the piece a king?
                // Is it the color we are looking for?
                if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    // At this point I should have the king found.
                    // And there is always a king so this code will always get to this point.
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // I need to implement validMoves before I can implement checkmate or stalemate.
        // Here is what I am thinking, use the pieceMoves method on the startPosition and
        // then loop through each of the moves to see if any of the moves put my team in check.
        // If they do, then i'll just not include that one in my new list.

        ChessPiece piece = board.getPiece(startPosition);
        // check rq to see if it is existent.
        if (piece == null) {
            return null;
        }

        // Okay, my list does not need to be ordered or anything so i'll just use collection.
        Collection<ChessMove> theoreticalMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        // I need to fill the validMoves collection with moves from theoreticalMoves that
        // do NOT put me in check.
        // I can just use the mountains for loop example from the collections lecture again
        // to go through the whole collection.
        for (var safeMove : theoreticalMoves) {
            // Okay so I should move the piece and see if it puts me in check,
            // then move it back. During the isInCheck if statement, I can
            // decide whether or not to add the move to validMoves.
            // ALSO, when I move the piece back, I cannot forget to "uncapture" whatever
            // piece I just removed from the board.
            ChessPosition endPosition = safeMove.getEndPosition();

            // Okay, let's save our current piece and the piece we're about to capture before
            // the test move is made.
            ChessPiece testPiece = board.getPiece(startPosition);
            ChessPiece enemyPiece = board.getPiece(endPosition);

            if (kingIsSafe(startPosition, endPosition, testPiece, enemyPiece)) {
                validMoves.add(safeMove);
            }
        }

        EnPassantHelper.addEnPassant(this, piece, startPosition, lastMove, board, validMoves);

        // Okay here is my thought process.
        // 1. Make sure the king is not currently in check.
        // 2. Check castling fields.
        // 3. Make sure in between squares are empty.
        // 4. Make sure the king doesn't pass through check or wind up in check.
        // 5. If that's all good, then the king can castle!
        if (piece.getPieceType() == ChessPiece.PieceType.KING && !isInCheck(piece.getTeamColor())) {
            // White's piece.
            // King has not moved.
            if (piece.getTeamColor() == TeamColor.WHITE && startPosition.equals(new ChessPosition(1, 5)) && whiteKingCanCastle) {
                checkAndAddCastlingMoves(validMoves, piece, startPosition, 1, whiteLeftRookCanCastle, whiteRightRookCanCastle);
            }
            // Black's piece.
            // King has not moved.
            else if (piece.getTeamColor() == TeamColor.BLACK && startPosition.equals(new ChessPosition(8, 5)) && blackKingCanCastle) {
                checkAndAddCastlingMoves(validMoves, piece, startPosition, 8, blackLeftRookCanCastle, blackRightRookCanCastle);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Here is my thought process.
        // 1. Make a collection of all the valid moves from that piece's position.
        // 2. Check and see if the user's move is on that list.
        // 3. If so, I'll make the move and then swith turns.
        // 4. If not, then I'll throw invalidMove and not switch turns.

        Collection<ChessMove> availableMoves = validMoves(move.getStartPosition());

        // Make sure the user didn't try to move an empty square.
        if (availableMoves == null) {
            throw new InvalidMoveException();
        }

        // Make sure the piece that is being moves is on the same team as whoever's team it is.
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException();
        }

        // onto the loop.
        for (var possibleMove : availableMoves) {
            // I supppose .equals is gonna be valid here.
            if (possibleMove.equals(move)) {
                piece = board.getPiece(move.getStartPosition());
                board.addPiece(move.getStartPosition(), null);

                // Check for en passant.
                // I need to know if the pawn is moving to an empty square and if the movement is diagonal.
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(move.getEndPosition()) == null) {
                    // Empty square, now are we moving diagonally?
                    if (move.getStartPosition().getColumn() != move.getEndPosition().getColumn()) {
                        // Okay this is en passant.
                        // I need to take out the enemy pawn, which should be at last position.
                        board.addPiece(lastMove.getEndPosition(), null);
                    }
                }

                // Promotion check.
                if (move.getPromotionPiece() != null) {
                    piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                }

                // Castling
                // The piece being moved is a king.
                if (piece.getPieceType() == ChessPiece.PieceType.KING && move.getStartPosition().getColumn() == 5) {
                    int row = (piece.getTeamColor() == TeamColor.WHITE) ? 1 : 8;
                    // Castling Right
                    if (move.getEndPosition().getColumn() == 7) {
                        executeCastleRookMove(row, 8, 6);
                    }
                    // Castling left
                    else if (move.getEndPosition().getColumn() == 3) {
                        executeCastleRookMove(row, 1, 4);
                    }
                }

                board.addPiece(move.getEndPosition(), piece);

                // Switch Turns
                setTeamTurn((teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);

                // need to update to the most recent move.
                lastMove = move;

                // okay just need a last minute section for updating fields.
                //  the king moved, then let's make his castling ability false.
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (piece.getTeamColor() == TeamColor.WHITE) {
                        whiteKingCanCastle = false;
                    } else {
                        blackKingCanCastle = false;
                    }
                }

                updateRookCastlingFlags(move.getStartPosition());
                updateRookCastlingFlags(move.getEndPosition());

                // phase 6 addition.
                if (isInCheckmate(teamTurn) || isInStalemate(teamTurn)) {
                    gameOver = true;
                }

                return;
            }
        }
        throw new InvalidMoveException();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingCurrentLocation = findKingPosition(teamColor);
        if (kingCurrentLocation == null) {
            return false;
        }

        // Now that I know where the king is, I suppose I should just scan the
        // entire enemy team and see if any of them have the king's location in their set of
        // pieceMoves? It feels horrifically inefficient, but I'm not storing the location
        // of any of the pieces, so I legit have no better way than to just scan the whole
        // board each time.
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                // okay let's get this location stored.
                ChessPosition location = new ChessPosition(r, c);
                ChessPiece enemy = board.getPiece(location);

                // Is there a piece here?
                // Is the piece an enemy?
                if (enemy == null || enemy.getTeamColor() == teamColor) {
                    continue;
                }

                // Okay this is an enemy piece. So I can just call pieceMoves on them?
                // I need to store the moves and check them all somehow.
                Collection<ChessMove> attacks = enemy.pieceMoves(board, location);

                // Using mountains example from lecture, for loops can get everything in a collection one at a time.
                for (var move : attacks) {
                    // Not sure why .equals works here and not anywhere else.
                    if (move.getEndPosition().equals(kingCurrentLocation)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return !teamHasLegalMoves(teamColor); // Fancy writing if I do say so myself
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        return !teamHasLegalMoves(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        // Okay obviously board cannot be private final.
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        // I suspect that I am just returning board here.
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return gameOver == chessGame.gameOver
                && teamTurn == chessGame.teamTurn
                && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board, gameOver);
    }
}