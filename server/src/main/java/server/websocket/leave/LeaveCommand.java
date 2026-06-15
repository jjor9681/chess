// not going to implement just yet. I need to see if things compile.

package server.websocket.leave;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ConnectionManagerPlus;
import server.websocket.ErrorSender;
import websocket.commands.UserGameCommand;

public class LeaveCommand {

    private final ErrorSender errorSender;

    public LeaveCommand(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ConnectionManagerPlus connectionManager,
            ErrorSender errorSender) {

        this.errorSender = errorSender;
    }

    public void execute(
            UserGameCommand command,
            Session session) {

        errorSender.send(
                session,
                "Error: leave command not implemented yet");
    }
}