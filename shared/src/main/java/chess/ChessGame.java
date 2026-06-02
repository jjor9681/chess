// Okay I have read the instructions and am trying to understand how I should start.

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
        if (piece == null){
            return null;
        }

        // Okay, my list does not need to be ordered or anything so i'll just use collection.
        Collection<ChessMove> theoreticalMoves = piece.pieceMoves(board,startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        // I need to fill the validMoves collection with moves from theoreticalMoves that
        // do NOT put me in check.
        // I can just use the mountains for loop example from the collections lecture again
        // to go through the whole collection.
        for (var safeMove: theoreticalMoves){
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

            // Okie dokie, now we make the move.
            // Pick up the old piece.
            board.addPiece(startPosition,null);
            // Place down the new one.
            board.addPiece(endPosition, testPiece);

            // High key there is a moment above where the piece does not exist for a moment.
            // I have to choose between that or having two of the same piece at the same time.
            // not sure if it matters but this is a note to my future self if problems like that occur.

            // Okay, now if we are NOT in check, let's add safeMove to validMoves.
            if (!isInCheck(testPiece.getTeamColor())){
                validMoves.add(safeMove);
            }

            // Time to restore the board.
            board.addPiece(startPosition, testPiece);
            board.addPiece(endPosition, enemyPiece);

            // Another note to my future self. Not totally sure if enemyPiece ends up being null
            // what will end up happening. Hopefully it just works.

        }
        // En passant extra credit
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN){ // I need to include ChessPiece. because I am no longer in ChessPiece.java
            // Make sure that it's not null or this will crash at the beginning of the game haha
            if (lastMove != null){
                // Who most recently moved?
                ChessPiece possiblePawn = board.getPiece(lastMove.getEndPosition());
                // Are they an enemy pawn?
                if (possiblePawn.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() != possiblePawn.getTeamColor()){
                    // okay I need to do some math here. In ECEn 320 I did it like this:
                    int enemyRowMovement = Math.abs(lastMove.getStartPosition().getRow() - lastMove.getEndPosition().getRow());
                    int adjacentDistance = lastMove.getEndPosition().getRow() - startPosition.getRow();
                    int strikingDistance = Math.abs(lastMove.getEndPosition().getColumn() - startPosition.getColumn());
                    if (enemyRowMovement == 2){ // Enemy pawn double moved.
                        if (adjacentDistance == 0) { // Pawns on the same row.
                            if (strikingDistance == 1) { // Pawns are side by side.
                                // Okay we know that the pawns meet the enPassant conditions.
                                // Now I need to check behind the enemy pawn and make sure
                                // that the square there is empty.
                                // Actually, sike! I know the square there is empty because the pawn wouldn't
                                // have been able to double move.
                                int newRow;
                                if (piece.getTeamColor() == TeamColor.WHITE){
                                    newRow = 6;
                                } else {
                                    newRow = 3;
                                }
                                int newCol = lastMove.getEndPosition().getColumn();

                                //Combine for the new chess position.
                                ChessPosition enPassantEndPosition = new ChessPosition(newRow,newCol);
                                ChessMove enPassant = new ChessMove(startPosition, enPassantEndPosition, null); // There won't ever be a promotion on this move.

                                // Now I should simulate the move and see if it puts me in check.
                                ChessPosition dyingPawnPosition = lastMove.getEndPosition();
                                board.addPiece(startPosition,null);
                                board.addPiece(dyingPawnPosition, null);
                                board.addPiece(enPassantEndPosition, piece);

                                // If not in check, en passant is valid!
                                if (!isInCheck(piece.getTeamColor())){
                                    validMoves.add(enPassant);
                                }
                                board.addPiece(startPosition,piece);
                                board.addPiece(dyingPawnPosition, possiblePawn);
                                board.addPiece(enPassantEndPosition, null);
                            }
                        }
                    }
                }
            }
        }

        if (piece.getPieceType() == ChessPiece.PieceType.KING){
            // Okay here is my thought process.
            // 1. Make sure the king is not currently in check.
            // 2. Check castling fields.
            // 3. Make sure in between squares are empty.
            // 4. Make sure the king doesn't pass through check or wind up in check.
            // 5. If that's all good, then the king can castle!

            if (!isInCheck(piece.getTeamColor())){
                if (piece.getTeamColor() == TeamColor.WHITE){ // White's piece.
                    if (whiteKingCanCastle) { // King has not moved.
                        if (whiteLeftRookCanCastle){
                            // are the in between squares all empty?
                            ChessPosition leftTwo = new ChessPosition(1,2);
                            ChessPosition leftThree = new ChessPosition(1,3);
                            ChessPosition leftFour = new ChessPosition(1,4);
                            if (board.getPiece(leftTwo) == null && board.getPiece(leftThree) == null && board.getPiece(leftFour) == null){ // Row is empty. Now time to simulate checks.
                                board.addPiece(startPosition, null);
                                board.addPiece(leftFour, piece);
                                if (!isInCheck(piece.getTeamColor())) {
                                    board.addPiece(leftFour, null);
                                    board.addPiece(leftThree, piece);
                                    if (!isInCheck(piece.getTeamColor())) {
                                        validMoves.add(new ChessMove(startPosition, leftThree, null));
                                        board.addPiece(leftThree, null);
                                        board.addPiece(startPosition, piece);
                                    } else {
                                        board.addPiece(leftThree, null);
                                    }
                                } else {
                                    board.addPiece(leftFour, null);
                                }
                                board.addPiece(startPosition, piece);
                            }
                        }
                        if (whiteRightRookCanCastle) {
                            // are the in between squares all empty?
                            ChessPosition rightSix = new ChessPosition(1,6);
                            ChessPosition rightSeven = new ChessPosition(1,7);
                            if (board.getPiece(rightSeven) == null && board.getPiece(rightSix) == null){ // Row is empty. Now time to simulate checks.
                                board.addPiece(startPosition, null);
                                board.addPiece(rightSix, piece);
                                if (!isInCheck(piece.getTeamColor())) {
                                    board.addPiece(rightSix, null);
                                    board.addPiece(rightSeven, piece);
                                    if (!isInCheck(piece.getTeamColor())) {
                                        validMoves.add(new ChessMove(startPosition, rightSeven, null));
                                        board.addPiece(rightSeven, null);
                                        board.addPiece(startPosition, piece);
                                    } else {
                                        board.addPiece(rightSeven, null);
                                    }
                                } else {
                                    board.addPiece(rightSix, null);
                                }
                                board.addPiece(startPosition, piece);
                            }
                        }
                    }
                } else { // Black's piece.
                    if (blackKingCanCastle) { // King has not moved.
                        if (blackLeftRookCanCastle){
                            // are the in between squares all empty?
                            ChessPosition leftTwo = new ChessPosition(8,2);
                            ChessPosition leftThree = new ChessPosition(8,3);
                            ChessPosition leftFour = new ChessPosition(8,4);
                            if (board.getPiece(leftTwo) == null && board.getPiece(leftThree) == null && board.getPiece(leftFour) == null){ // Row is empty. Now time to simulate checks.
                                board.addPiece(startPosition, null);
                                board.addPiece(leftFour, piece);
                                if (!isInCheck(piece.getTeamColor())) {
                                    board.addPiece(leftFour, null);
                                    board.addPiece(leftThree, piece);
                                    if (!isInCheck(piece.getTeamColor())) {
                                        validMoves.add(new ChessMove(startPosition, leftThree, null));
                                        board.addPiece(leftThree, null);
                                        board.addPiece(startPosition, piece);
                                    } else {
                                        board.addPiece(leftThree, null);
                                    }
                                } else {
                                    board.addPiece(leftFour, null);
                                }
                                board.addPiece(startPosition, piece);
                            }
                        }
                        if (blackRightRookCanCastle) {
                            // are the in between squares all empty?
                            ChessPosition rightSix = new ChessPosition(8,6);
                            ChessPosition rightSeven = new ChessPosition(8,7);
                            if (board.getPiece(rightSeven) == null && board.getPiece(rightSix) == null){ // Row is empty. Now time to simulate checks.
                                board.addPiece(startPosition, null);
                                board.addPiece(rightSix, piece);
                                if (!isInCheck(piece.getTeamColor())) {
                                    board.addPiece(rightSix, null);
                                    board.addPiece(rightSeven, piece);
                                    if (!isInCheck(piece.getTeamColor())) {
                                        validMoves.add(new ChessMove(startPosition, rightSeven, null));
                                        board.addPiece(rightSeven, null);
                                        board.addPiece(startPosition, piece);
                                    } else {
                                        board.addPiece(rightSeven, null);
                                    }
                                } else {
                                    board.addPiece(rightSix, null);
                                }
                                board.addPiece(startPosition, piece);
                            }
                        }
                    }
                }
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
        if (availableMoves == null){
            throw new InvalidMoveException();
        }
        // Make sure the piece that is being moves is on the same team as whoever's team it is.
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException();
        }

        // onto the loop.
        for (var possibleMove : availableMoves){
            if (possibleMove.equals(move)){ // I supppose .equals is gonna be valid here.
                piece = board.getPiece(move.getStartPosition());
                board.addPiece(move.getStartPosition(),null);

                if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
                    // I need to know if the pawn is moving to an empty square and if the movement is diagonal.
                    if (board.getPiece(move.getEndPosition()) == null){ // Empty square, now are we moving diagonally?
                        if (move.getStartPosition().getColumn() != move.getEndPosition().getColumn()){ // Okay this is en passant.
                            // I need to take out the enemy pawn, which should be at last position.
                            board.addPiece(lastMove.getEndPosition(),null);
                        }
                    }
                }
                // Promotion check.
                if (move.getPromotionPiece() != null){
                    piece = new ChessPiece(piece.getTeamColor(),move.getPromotionPiece());
                }
                board.addPiece(move.getEndPosition(),piece);

                // Switch Turns
                if (teamTurn == TeamColor.WHITE){
                    setTeamTurn(TeamColor.BLACK);
                } else {
                    setTeamTurn(TeamColor.WHITE);
                }

                lastMove = move; // need to update to the most recent move.

                // okay just need a last minute section for updating fields.
                //  the king moved, then let's make his castling ability false.
                if (piece.getPieceType() == ChessPiece.PieceType.KING){
                    if (piece.getTeamColor() == TeamColor.WHITE){
                        whiteKingCanCastle = false;
                    } else {
                        blackKingCanCastle = false;
                    }
                }
                // If a rook has moved, it can no longer castle with the king.

                if (piece.getPieceType() == ChessPiece.PieceType.ROOK && piece.getTeamColor() == TeamColor.WHITE){
                    if (move.getStartPosition().getColumn() == 1){
                        whiteLeftRookCanCastle = false;
                    }
                    else if (move.getStartPosition().getColumn() == 8){
                        whiteRightRookCanCastle = false;
                    }
                } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK && piece.getTeamColor() == TeamColor.BLACK){
                    if (move.getStartPosition().getColumn() == 1){
                        blackLeftRookCanCastle = false;
                    }
                    else if (move.getStartPosition().getColumn() == 8){
                        blackRightRookCanCastle = false;
                    }
                }
                // Now I am worried about someone promoting to a rook, and then moving it to a square that can castle.
                // If a corner square is ever captured, then this can happen. So I need to update the fields when a corner Rook gets captured.
                if (move.getEndPosition().getRow() == 1 && move.getEndPosition().getColumn() == 1){
                    whiteLeftRookCanCastle = false;
                }
                else if (move.getEndPosition().getRow() == 1 && move.getEndPosition().getColumn() == 8){
                    whiteRightRookCanCastle = false;
                }
                else if (move.getEndPosition().getRow() == 8 && move.getEndPosition().getColumn() == 1){
                    blackLeftRookCanCastle = false;
                }
                else if (move.getEndPosition().getRow() == 8 && move.getEndPosition().getColumn() == 8){
                    blackRightRookCanCastle = false;
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
        // How do I know where the King is? I don't know how to track him.
        // Do I store a variable with his current location?
        // Or should I just scan the board every time for him with nested loops?
        // Let's try that.

        // If I find him, i'll store his location in this variable.
        ChessPosition kingCurrentLocation = null;

        // Now the nested for loops.
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                // Okay let's get the piece at this location.
                ChessPosition scanSpot = new ChessPosition(r, c);
                ChessPiece mysteryPiece = board.getPiece(scanSpot);

                // Is there actually a piece there?
                if (mysteryPiece == null) {
                    continue;
                }

                // Is the piece a king?
                if (mysteryPiece.getPieceType() != ChessPiece.PieceType.KING) {
                    continue;
                }

                // Is it the color we are looking for?
                if (mysteryPiece.getTeamColor() != teamColor) {
                    continue;
                }

                // At this point I should have the king found.
                // And there is always a king so this code will always get to this point.
                kingCurrentLocation = scanSpot;
            }
        }

        // Now that I know where the king is, I suppose I should just scan the
        // entire enemy team and see if any of them have the king's location in their set of
        // pieceMoves? It feels horrifically inefficient, but I'm not storing the location
        // of any of the pieces, so I legit have no better way than to just scan the whole
        // board each time.
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                // okay let's get this location stored.
                ChessPosition location = new ChessPosition(r,c);
                ChessPiece enemy = board.getPiece(location);

                // Is there a piece here?
                if (enemy == null){
                    continue;
                }

                // Is the piece an enemy?
                if (enemy.getTeamColor() == teamColor){
                    continue;
                }

                // Okay this is an enemy piece. So I can just call pieceMoves on them?
                // I need to store the moves and check them all somehow.
                Collection<ChessMove> attacks = enemy.pieceMoves(board,location);

                // Okay I have all their moves, but how do I access the collection?
                // Okay the collections.md lecture has an example of how to access
                // "mountains" ... It is "for(var m: mountains){print (m);}" and that gets them all.
                // I can do the same thing I bet...
                for (var move: attacks){
                    if (move.getEndPosition().equals(kingCurrentLocation)){ // Not sure why .equals works here and not anywhere else.
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
        // I probably need validMoves working in order for this to work.
        // Okay I remember doing this in ECEn 320.
        // If that team's king is in check, and they have no other valid moves, then checkmate.

        // Are we in check?
        if (isInCheck(teamColor)){

            // Check all of this team's pieces for valid moves.
            for (int r = 1; r <= 8; r++) {
                for (int c = 1; c <= 8; c++) {
                    ChessPosition position = new ChessPosition(r, c);
                    ChessPiece piece = board.getPiece(position);

                    if (piece == null) { // Nobody was there.
                        continue;
                    }
                    if (piece.getTeamColor() != teamColor) { // Wrong team.
                        continue;
                    }

                    Collection<ChessMove> moves = validMoves(position);
                    // I need to know if moves is empty.
                    int i = 0;
                    // Not sure how to do it with an if statement.
                    for (var move : moves){
                        i += 1;
                    }
                    if (i > 0){ // Somebody somewhere can move. Return false.
                        return false;
                    }
                }
            }
        } else { // if i'm not in check, I need to return false.
            return false;
        }
        // and that's the game!
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // Same thing as checkmate, just that the team is not in check.
        // I can just copy paste and add a ! to the initial if statement.

        // Are we NOT in check?
        if (!isInCheck(teamColor)){

            // Check all of this team's pieces for valid moves.
            for (int r = 1; r <= 8; r++) {
                for (int c = 1; c <= 8; c++) {
                    ChessPosition position = new ChessPosition(r, c);
                    ChessPiece piece = board.getPiece(position);

                    if (piece == null) { // Nobody was there.
                        continue;
                    }
                    if (piece.getTeamColor() != teamColor) { // Wrong team.
                        continue;
                    }

                    Collection<ChessMove> moves = validMoves(position);
                    // I need to know if moves is empty.
                    int i = 0;
                    // Not sure how to do it with an if statement.
                    for (var move : moves){
                        i += 1;
                    }
                    if (i > 0){ // Somebody somewhere can move. Return false.
                        return false;
                    }
                }
            }
        } else { // if i'm IN check, I need to return false.
            return false;
        }
        // and that's the game!
        return true;
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
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
