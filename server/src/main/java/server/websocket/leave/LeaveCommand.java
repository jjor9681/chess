package server.websocket.leave;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ConnectionManagerPlus;
import server.websocket.ErrorSender;
import websocket.commands.UserGameCommand;

public class LeaveCommand {

    private final LeaveValidator leaveValidator;
    private final LeaveMaker leaveMaker;
    private final LeaveCommunicator leaveCommunicator;
    private final ErrorSender errorSender;

    public LeaveCommand(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ConnectionManagerPlus connectionManager,
            ErrorSender errorSender) {

        leaveValidator =
                new LeaveValidator(
                        authDAO,
                        gameDAO,
                        errorSender);

        leaveMaker =
                new LeaveMaker(
                        gameDAO);

        leaveCommunicator =
                new LeaveCommunicator(
                        connectionManager);

        this.errorSender =
                errorSender;
    }

    public void execute(
            UserGameCommand command,
            Session session) {

        try {

            LeaveValidator.ValidLeaveData leaveData =
                    leaveValidator.validate(
                            command,
                            session);

            if (leaveData == null) {
                return;
            }

            leaveMaker.leave(
                    leaveData.gameData(),
                    leaveData.authData().username());

            leaveCommunicator.sendLeaveMessage(
                    command,
                    session,
                    leaveData.authData());

        } catch (Exception ex) {

            errorSender.send(
                    session,
                    "Error: " + ex.getMessage());
        }
    }
}