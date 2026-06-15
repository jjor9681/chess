// is resigning currenlty allowed

package server.websocket.resign;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ErrorSender;
import websocket.commands.UserGameCommand;

public class ResignValidator {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final ErrorSender errorSender;

    public ResignValidator(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ErrorSender errorSender) {

        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.errorSender = errorSender;
    }

    public ValidResignData validate(
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

        if (!isPlayer(
                authData.username(),
                gameData)) {

            errorSender.send(
                    session,
                    "Error: observers cannot resign");
            return null;
        }

        if (gameData.game().isGameOver()) {
            errorSender.send(
                    session,
                    "Error: game is already over");
            return null;
        }

        return new ValidResignData(
                authData,
                gameData);
    }

    private boolean isPlayer(
            String username,
            GameData gameData) {

        return username.equals(gameData.whiteUsername())
                || username.equals(gameData.blackUsername());
    }

    public record ValidResignData(
            AuthData authData,
            GameData gameData
    ) {}
}