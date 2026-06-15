package server.websocket.resign;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ConnectionManagerPlus;
import server.websocket.ErrorSender;
import websocket.commands.UserGameCommand;

public class ResignCommand {

    private final ResignValidator resignValidator;
    private final ResignMaker resignMaker;
    private final ResignCommunicator resignCommunicator;
    private final ErrorSender errorSender;

    public ResignCommand(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ConnectionManagerPlus connectionManager,
            ErrorSender errorSender) {
        this.resignValidator =
                new ResignValidator(
                        authDAO,
                        gameDAO,
                        errorSender);
        this.resignMaker =
                new ResignMaker(
                        gameDAO);
        this.resignCommunicator =
                new ResignCommunicator(
                        connectionManager);
        this.errorSender =
                errorSender;
    }
    public void execute(
            UserGameCommand command,
            Session session) {
        try {
            ResignValidator.ValidResignData resignData =
                    resignValidator.validate(
                            command,
                            session);
            if (resignData == null) {
                return;
            }
            resignMaker.resign(
                    resignData.gameData());
            resignCommunicator.sendResignMessage(
                    command,
                    resignData.authData());
        } catch (Exception ex) {
            errorSender.send(
                    session,
                    "Error: " + ex.getMessage());
        }
    }
}