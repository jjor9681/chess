package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class GameBuilder {

    public String buildWhiteBoard() {
        ChessGame game = new ChessGame();
        return buildBoard(game.getBoard(), true);
    }

    public String buildBlackBoard() {
        ChessGame game = new ChessGame();
        return buildBoard(game.getBoard(), false);
    }

    private String buildBoard(ChessBoard board, boolean whitePerspective) {
        StringBuilder result = new StringBuilder();

        result.append(RESET_TEXT_COLOR).append(RESET_BG_COLOR).append("\n");

        appendColumnLabels(result, whitePerspective);

        if (whitePerspective) {
            for (int row = 8; row >= 1; row--) {
                appendRow(result, board, row, true);
            }
        } else {
            for (int row = 1; row <= 8; row++) {
                appendRow(result, board, row, false);
            }
        }

        appendColumnLabels(result, whitePerspective);

        result.append(RESET_TEXT_COLOR).append(RESET_BG_COLOR).append("\n");

        return result.toString();
    }

    private void appendColumnLabels(StringBuilder result, boolean whitePerspective) {
        result.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_WHITE).append("   ");

        if (whitePerspective) {
            for (char col = 'a'; col <= 'h'; col++) {
                result.append(" ").append(col).append(" ");
            }
        } else {
            for (char col = 'h'; col >= 'a'; col--) {
                result.append(" ").append(col).append(" ");
            }
        }

        result.append("\n");
    }

    private void appendRow(StringBuilder result, ChessBoard board, int row, boolean whitePerspective) {
        result.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_WHITE).append(" ").append(row).append(" ");

        if (whitePerspective) {
            for (int col = 1; col <= 8; col++) {
                appendSquare(result, board, row, col);
            }
        } else {
            for (int col = 8; col >= 1; col--) {
                appendSquare(result, board, row, col);
            }
        }

        result.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_WHITE).append(" ").append(row).append("\n");
    }

    private void appendSquare(StringBuilder result, ChessBoard board, int row, int col) {
        boolean lightSquare = (row + col) % 2 != 0;

        result.append(lightSquare ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREEN);

        ChessPiece piece = board.getPiece(new ChessPosition(row, col));

        result.append(pieceText(piece));
    }

    private String pieceText(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }

        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}