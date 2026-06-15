// make sure the game exists and the leaving player is real.

package server.websocket.leave;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ErrorSender;
import websocket.commands.UserGameCommand;

public class LeaveValidator {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final ErrorSender errorSender;

    public LeaveValidator(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ErrorSender errorSender) {

        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.errorSender = errorSender;
    }
    public ValidLeaveData validate(
            UserGameCommand command,
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
        return new ValidLeaveData(
                authData,
                gameData);
    }

    public record ValidLeaveData(
            AuthData authData,
            GameData gameData
    ) {}
}