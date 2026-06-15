// Basically a pet shop copy paste.
// I am going to build it as the server side object that receives websocket messages from /ws
// all it does is talk to javalin websocket events.
// Does serious delegation to command controller.

package server.websocket;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final CommandController commandController;

    public WebSocketHandler(
            AuthDAO authDAO,
            GameDAO gameDAO) {

        ConnectionManagerPlus connectionManager =
                new ConnectionManagerPlus();

        commandController =
                new CommandController(
                        authDAO,
                        gameDAO,
                        connectionManager);
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        ctx.enableAutomaticPings();
        System.out.println("WebSocket connected");
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        commandController.handle(
                ctx.message(),
                ctx.session);
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("WebSocket closed");
    }
}
