// receives raw json and deserializes into a game command.
// sends it to the correct file for action.

package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import server.websocket.move.MoveCommand;

public class CommandController {

    private final ConnectCommand connectCommand;
    private final MoveCommand moveCommand;
    private final LeaveCommand leaveCommand;
    private final ResignCommand resignCommand;
    private final ErrorSender errorSender;

    private final Gson gson = new Gson();

    public CommandController(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ConnectionManagerPlus connectionManager) {

        errorSender =
                new ErrorSender(
                        connectionManager);

        connectCommand =
                new ConnectCommand(
                        authDAO,
                        gameDAO,
                        connectionManager,
                        errorSender);

        moveCommand =
                new MoveCommand(
                        authDAO,
                        gameDAO,
                        connectionManager,
                        errorSender);

        leaveCommand =
                new LeaveCommand(
                        authDAO,
                        gameDAO,
                        connectionManager,
                        errorSender);

        resignCommand =
                new ResignCommand(
                        authDAO,
                        gameDAO,
                        connectionManager,
                        errorSender);
    }

    public void handle(
            String rawMessage,
            Session session) {

        try {
            UserGameCommand command =
                    gson.fromJson(
                            rawMessage,
                            UserGameCommand.class);

            switch (command.getCommandType()) {
                case CONNECT -> connectCommand.execute(
                        command,
                        session);

                case MAKE_MOVE -> moveCommand.execute(
                        rawMessage,
                        session);

                case LEAVE -> leaveCommand.execute(
                        command,
                        session);

                case RESIGN -> resignCommand.execute(
                        command,
                        session);
            }

        } catch (Exception ex) {
            errorSender.send(
                    session,
                    "Error: " + ex.getMessage());
        }
    }
}