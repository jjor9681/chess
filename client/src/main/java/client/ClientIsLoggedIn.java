package client;

import serverfacade.ServerFacade;

public class ClientIsLoggedIn {

    private final ServerFacade server;
    public ClientIsLoggedIn(ServerFacade server) {
        this.server = server;
    }
    private LogoutResult createGame(
            String[] params,
            String authToken) throws Exception {
        if (params.length < 1) {
            return new LogoutResult(
                    false,
                    "Expected: create <game name>\n");
        }
        String gameName = String.join(" ", params);
        server.createGame(authToken, gameName);
        return new LogoutResult(
                false,
                "Created game: " + gameName + "\n");
    }

    private LogoutResult listGames(String authToken) throws Exception {
        var games = server.listGames(authToken);
        if (games.isEmpty()) {
            return new LogoutResult(
                    false,
                    "No games available.\n");
        }
        StringBuilder result = new StringBuilder();
        int number = 1;
        for (var game : games) {
            result.append(number)
                    .append(". ")
                    .append(game.gameName())
                    .append(" | White: ")
                    .append(game.whiteUsername())
                    .append(" | Black: ")
                    .append(game.blackUsername())
                    .append("\n");
            number++;
        }
        return new LogoutResult(
                false,
                result.toString());
    }

    public LogoutResult eval(
            String command,
            String[] params,
            String authToken) throws Exception {

        return switch (command) {
            case "logout" -> logout(authToken);
            case "create" -> createGame(params, authToken);
            case "list" -> listGames(authToken);
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