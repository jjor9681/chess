// not going to implement just yet. I need to see if things compile.

package server.websocket;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;

public class MoveCommand {

    private final ErrorSender errorSender;

    public MoveCommand(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ConnectionManagerPlus connectionManager,
            ErrorSender errorSender) {

        this.errorSender = errorSender;
    }

    public void execute(
            String rawMessage,
            Session session) {

        errorSender.send(
                session,
                "Error: move command not implemented yet");
    }
}