package client;

import serverfacade.ServerFacade;

public class ClientIsLoggedIn {

    private final ServerFacade server;

    public ClientIsLoggedIn(ServerFacade server) {
        this.server = server;
    }

    public LogoutResult eval(
            String command,
            String[] params,
            String authToken) throws Exception {

        return switch (command) {
            case "logout" -> logout(authToken);
            default -> new LogoutResult(
                    false,
                    "Unknown command. Type help.\n");
        };
    }

    private LogoutResult logout(String authToken) throws Exception {
        server.logout(authToken);

        return new LogoutResult(
                true,
                "Logged out.\n");
    }

    public String help() {
        return """
                Commands:
                  logout
                  create <game name>
                  list
                  play <game number> <WHITE|BLACK>
                  observe <game number>
                  help
                  quit
                """;
    }

    public record LogoutResult(
            boolean loggedOut,
            String message
    ) {}
}