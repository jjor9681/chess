// Okay this thing is the true beast. how is this gonna work..
// it needs to parse json moves.
// deal with authTokens, gameID's, make sure players are playing,
// make sure moves are legal
// make sure nobody has lost
// use makemove.
// Save updated game to the db
// LOAD_GAME needs to go to everyone, including observers.
// send NOTIFICATION to everyone except whoever just took their turn.
// probably should let someone know when they lost or are in check.

// There will be some serious delegation to other files.
package server.websocket.move;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.ConnectionManagerPlus;
import server.websocket.ErrorSender;
import websocket.commands.MakeMoveCommand;

public class MoveCommand {

    private final MoveValidator moveValidator;
    private final MoveMaker moveMaker;
    private final MoveCommunicator moveCommunicator;
    private final ErrorSender errorSender;
    private final Gson gson = new Gson();

    public MoveCommand(
            AuthDAO authDAO,
            GameDAO gameDAO,
            ConnectionManagerPlus connectionManager,
            ErrorSender errorSender) {

        this.moveValidator =
                new MoveValidator(
                        authDAO,
                        gameDAO,
                        errorSender);

        this.moveMaker =
                new MoveMaker(
                        gameDAO);

        this.moveCommunicator =
                new MoveCommunicator(connectionManager);

        this.errorSender =
                errorSender;
    }

    public void execute(
            String rawMessage,
            Session session) {

        try {
            MakeMoveCommand command =
                    gson.fromJson(
                            rawMessage,
                            MakeMoveCommand.class);

            MoveValidator.ValidMoveData moveData =
                    moveValidator.validate(
                            command,
                            session);

            if (moveData == null) {
                return;
            }

            var updatedGame =
                    moveMaker.makeMove(
                            command,
                            moveData.gameData());

            moveCommunicator.sendMoveMessages(
                    command,
                    session,
                    moveData.authData(),
                    moveData.gameData(),
                    updatedGame);

        } catch (Exception ex) {
            errorSender.send(
                    session,
                    "Error: " + ex.getMessage());
        }
    }
}