//Connects ChessClient and ClientIsLoggedIn

package client.gameplay;

import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;

public class GameplayClient implements NotificationHandler {

    private final WebSocketFacade webSocket;
    private final Board board;
    private final MessageHandler messageHandler;
    private final Moves moves;
    private final Help help = new Help();
    private final String perspective;

    private final String authToken;
    private final int gameID;

    public GameplayClient(
            String serverUrl,
            String authToken,
            int gameID,
            String perspective) throws Exception {

        this.authToken = authToken;
        this.gameID = gameID;
        this.perspective = perspective;

        board =
                new Board(
                        perspective);

        messageHandler =
                new MessageHandler(
                        board);

        webSocket =
                new WebSocketFacade(
                        serverUrl,
                        this);

        moves =
                new Moves(
                        webSocket,
                        authToken,
                        gameID,
                        board,
                        perspective);

        webSocket.connect(
                authToken,
                gameID);
    }

    @Override
    public void notify(String rawMessage) {
        messageHandler.handle(
                rawMessage);
    }

    public Result eval(
            String command,
            String[] params) throws Exception {

        return switch (command) {
            case "help" -> new Result(
                    false,
                    help.text());

            case "redraw" -> redraw();

            case "move" -> moves.move(
                    params);

            case "highlight" -> moves.highlight(
                    params);

            case "leave" -> leave();

            case "resign" -> resign();

            default -> new Result(
                    false,
                    "Unknown command. Type help.\n");
        };
    }

    private Result redraw() {
        return new Result(
                false,
                board.redraw());
    }

    private Result leave() throws Exception {
        webSocket.leave(
                authToken,
                gameID);

        return new Result(
                true,
                "Left game.\n");
    }

    private Result resign() throws Exception {

        if (perspective.equals("OBSERVER")) {
            return new Result(
                    false,
                    "Observers cannot resign.\n");
        }

        webSocket.resign(
                authToken,
                gameID);

        return new Result(
                false,
                "Resign request sent.\n");
    }

    public String help() {
        return help.text();
    }
}