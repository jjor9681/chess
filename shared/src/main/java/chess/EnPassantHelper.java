package chess;

import java.util.Collection;

public class EnPassantHelper {

    public static void addEnPassant(ChessGame game,
                                    ChessPiece piece,
                                    ChessPosition startPosition,
                                    ChessMove lastMove,
                                    ChessBoard board,
                                    Collection<ChessMove> validMoves) {
        // En passant extra credit
        // I need to include ChessPiece. because I am no longer in ChessPiece.java
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && lastMove != null) {
            // Make sure that it's not null or this will crash at the beginning of the game haha
            // Who most recently moved?
            ChessPiece possiblePawn = board.getPiece(lastMove.getEndPosition());
            // Are they an enemy pawn?
            if (possiblePawn != null
                    && possiblePawn.getPieceType() == ChessPiece.PieceType.PAWN
                    && piece.getTeamColor() != possiblePawn.getTeamColor()) {
                // okay I need to do some math here. In ECEn 320 I did it like this:
                int enemyRowMovement = Math.abs(lastMove.getStartPosition().getRow() - lastMove.getEndPosition().getRow());
                int adjacentDistance = lastMove.getEndPosition().getRow() - startPosition.getRow();
                int strikingDistance = Math.abs(lastMove.getEndPosition().getColumn() - startPosition.getColumn());

                // Enemy pawn double moved.
                // Pawns on the same row.
                // Pawns are side by side.
                if (enemyRowMovement == 2 && adjacentDistance == 0 && strikingDistance == 1) {
                    // Okay we know that the pawns meet the enPassant conditions.
                    // Now I need to check behind the enemy pawn and make sure
                    // that the square there is empty.
                    // Actually, sike! I know the square there is empty because the pawn wouldn't
                    // have been able to double move.
                    int newRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 6 : 3;
                    int newCol = lastMove.getEndPosition().getColumn();

                    //Combine for the new chess position.
                    ChessPosition enPassantEndPosition = new ChessPosition(newRow, newCol);
                    // There won't ever be a promotion on this move.
                    ChessMove enPassant = new ChessMove(startPosition, enPassantEndPosition, null);

                    // Now I should simulate the move and see if it puts me in check.
                    ChessPosition dyingPawnPosition = lastMove.getEndPosition();
                    board.addPiece(startPosition, null);
                    board.addPiece(dyingPawnPosition, null);
                    board.addPiece(enPassantEndPosition, piece);

                    // If not in check, en passant is valid!
                    if (!game.isInCheck(piece.getTeamColor())) {
                        validMoves.add(enPassant);
                    }

                    // Put the board back exactly how it was!
                    board.addPiece(startPosition, piece);
                    board.addPiece(dyingPawnPosition, possiblePawn);
                    board.addPiece(enPassantEndPosition, null);
                }
            }
        }
    }
}