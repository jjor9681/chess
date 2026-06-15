package client.gameplay;

import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.WebSocketFacade;

public class Moves {

    private final WebSocketFacade webSocket;
    private final String authToken;
    private final int gameID;
    private final Board board;

    public Moves(
            WebSocketFacade webSocket,
            String authToken,
            int gameID,
            Board board) {

        this.webSocket = webSocket;
        this.authToken = authToken;
        this.gameID = gameID;
        this.board = board;
    }

    public Result move(String[] params) throws Exception {
        if (params.length != 2) {
            return new Result(
                    false,
                    "Expected: move <start> <end>\n");
        }

        ChessPosition start =
                parsePosition(params[0]);

        ChessPosition end =
                parsePosition(params[1]);

        ChessMove move =
                new ChessMove(
                        start,
                        end,
                        null);

        webSocket.makeMove(
                authToken,
                gameID,
                move);

        return new Result(
                false,
                "Move sent.\n");
    }

    public Result highlight(String[] params) throws Exception {
        if (params.length != 1) {
            return new Result(
                    false,
                    "Expected: highlight <position>\n");
        }

        ChessPosition position =
                parsePosition(params[0]);

        return new Result(
                false,
                board.highlight(position));
    }

    private ChessPosition parsePosition(String input) throws Exception {
        if (input.length() != 2) {
            throw new Exception(
                    "Position must look like e2.");
        }

        char file =
                Character.toLowerCase(
                        input.charAt(0));

        char rank =
                input.charAt(1);

        int column =
                stringToColumn(file);

        int row =
                stringToRow(rank);

        return new ChessPosition(
                row,
                column);
    }

    private int stringToColumn(char file) throws Exception {
        return switch (file) {
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> throw new Exception(
                    "Column must be a through h.");
        };
    }

    private int stringToRow(char rank) throws Exception {
        return switch (rank) {
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            default -> throw new Exception(
                    "Row must be 1 through 8.");
        };
    }
}