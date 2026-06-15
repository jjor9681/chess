package server.websocket.move;

import chess.ChessGame;
import chess.ChessPiece;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ErrorSender;
import websocket.commands.MakeMoveCommand;

public class MoveValidator {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final ErrorSender errorSender;

    public MoveValidator(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ErrorSender errorSender) {

        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.errorSender = errorSender;
    }

    public ValidMoveData validate(
            MakeMoveCommand command,
            Session session) throws Exception {
        AuthData authData =
                authDAO.getAuth(
                        command.getAuthToken());
        if (authData == null) {
            errorSender.send(
                    session,
                    "Error: unauthorized");
            return null;
        }
        GameData gameData =
                gameDAO.getGame(
                        command.getGameID());
        if (gameData == null) {
            errorSender.send(
                    session,
                    "Error: game not found");
            return null;
        }
        if (gameData.game().isGameOver()) {
            errorSender.send(
                    session,
                    "Error: game is over");
            return null;
        }
        ChessPiece piece =
                gameData.game()
                        .getBoard()
                        .getPiece(
                                command.getMove()
                                        .getStartPosition());
        if (piece == null) {
            errorSender.send(
                    session,
                    "Error: no piece at start position");
            return null;
        }
        if (!userOwnsPiece(
                authData.username(),
                gameData,
                piece)) {
            errorSender.send(
                    session,
                    "Error: cannot move opponent piece");
            return null;
        }
        return new ValidMoveData(
                authData,
                gameData);
    }
    private boolean userOwnsPiece(
            String username,
            GameData gameData,
            ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return username.equals(
                    gameData.whiteUsername());
        }
        return username.equals(
                gameData.blackUsername());
    }

    // Hopefully I don't get docked for putting this here.
    public record ValidMoveData(
            AuthData authData,
            GameData gameData
    ) {}
}