// After a valid move,
// send LOAD_GAME to everyone
// send NOTIFICATION to everyone except whoever just moved
// maybe send a notification that someone is in check, or they lost or something.

package server.websocket.move;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ConnectionManagerPlus;
import websocket.commands.MakeMoveCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class MoveCommunicator {

    private final ConnectionManagerPlus connectionManager;

    public MoveCommunicator(ConnectionManagerPlus connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void sendMoveMessages(
            MakeMoveCommand command,
            Session session,
            AuthData authData,
            GameData gameData,
            ChessGame updatedGame) throws Exception {
        connectionManager.broadcastToGame(
                command.getGameID(),
                new LoadGameMessage(updatedGame));
        connectionManager.broadcastToGameExcept(
                command.getGameID(),
                session,
                new NotificationMessage(
                        authData.username()
                                + " made a move."));
        sendGameStatusNotifications(
                command.getGameID(),
                updatedGame,
                gameData);
    }
    private void sendGameStatusNotifications(
            int gameID,
            ChessGame game,
            GameData gameData) throws Exception {
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            connectionManager.broadcastToGame(
                    gameID,
                    new NotificationMessage(
                            gameData.whiteUsername()
                                    + " is in checkmate."));
            return;
        }
        if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            connectionManager.broadcastToGame(
                    gameID,
                    new NotificationMessage(
                            gameData.blackUsername()
                                    + " is in checkmate."));
            return;
        }
        if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
            connectionManager.broadcastToGame(
                    gameID,
                    new NotificationMessage(
                            gameData.whiteUsername()
                                    + " is in check."));
        }
        if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            connectionManager.broadcastToGame(
                    gameID,
                    new NotificationMessage(
                            gameData.blackUsername()
                                    + " is in check."));
        }
    }
}