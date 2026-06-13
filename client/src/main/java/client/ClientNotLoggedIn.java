package client;

import model.AuthData;
import serverfacade.ServerFacade;

public class ClientNotLoggedIn {

    private final ServerFacade server;

    public ClientNotLoggedIn(ServerFacade server) {
        this.server = server;
    }

    public LoginResult eval(String command, String[] params) throws Exception {
        return switch (command) {
            case "register" -> register(params);
            case "login" -> login(params);
            default -> new LoginResult(
                    false,
                    null,
                    null,
                    "Unknown command. Type help.\n");
        };
    }

    private LoginResult register(String[] params) throws Exception {
        if (params.length != 3) {
            return new LoginResult(
                    false,
                    null,
                    null,
                    "Expected: register <username> <password> <email>\n");
        }

        AuthData authData =
                server.register(
                        params[0],
                        params[1],
                        params[2]);

        return new LoginResult(
                true,
                authData.authToken(),
                authData.username(),
                "Registered and logged in as " + authData.username() + ".\n");
    }

    private LoginResult login(String[] params) throws Exception {
        if (params.length != 2) {
            return new LoginResult(
                    false,
                    null,
                    null,
                    "Expected: login <username> <password>\n");
        }

        AuthData authData =
                server.login(
                        params[0],
                        params[1]);

        return new LoginResult(
                true,
                authData.authToken(),
                authData.username(),
                "Logged in as " + authData.username() + ".\n");
    }

    public String help() {
        return """
                Commands:
                  register <username> <password> <email>
                  login <username> <password>
                  help
                  quit
                """;
    }

    public record LoginResult(
            boolean loggedIn,
            String authToken,
            String username,
            String message
    ) {}
}