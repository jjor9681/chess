// receive message from facade
// update board whenever LOAD_GAME hits
// print notifications and errors.

package client.gameplay;

import com.google.gson.Gson;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class MessageHandler {

    private final Board board;
    private final Gson gson = new Gson();

    public MessageHandler(Board board) {
        this.board = board;
    }

    public void handle(String rawMessage) {
        ServerMessage baseMessage =
                gson.fromJson(
                        rawMessage,
                        ServerMessage.class);

        switch (baseMessage.getServerMessageType()) {
            case LOAD_GAME -> handleLoadGame(rawMessage);
            case ERROR -> handleError(rawMessage);
            case NOTIFICATION -> handleNotification(rawMessage);
        }
    }

    private void handleLoadGame(String rawMessage) {
        LoadGameMessage message =
                gson.fromJson(
                        rawMessage,
                        LoadGameMessage.class);

        board.updateGame(
                message.getGame());

        System.out.print(
                "\n" + board.redraw());
    }

    private void handleError(String rawMessage) {
        ErrorMessage message =
                gson.fromJson(
                        rawMessage,
                        ErrorMessage.class);

        System.out.print(
                "\n" + message.getErrorMessage() + "\n");
    }

    private void handleNotification(String rawMessage) {
        NotificationMessage message =
                gson.fromJson(
                        rawMessage,
                        NotificationMessage.class);

        System.out.print(
                "\n" + message.getMessage() + "\n");
    }
}