// not going to implement just yet. I need to see if things compile.

package server.websocket;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

public class ResignCommand {

    private final ErrorSender errorSender;

    public ResignCommand(
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
                "Error: resign command not implemented yet");
    }
}