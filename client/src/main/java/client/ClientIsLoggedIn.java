package client;

import model.GameData;
import serverfacade.ServerFacade;

import java.util.Collection;

public class ClientIsLoggedIn {

    private final ServerFacade server;
    private Collection<GameData> gamesListMemory;

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

    private GameData getGameFromListNumber(int listNumber) throws Exception {
        if (gamesListMemory == null || gamesListMemory.isEmpty()) {
            throw new Exception("You need to the the games list first.");
        }
        if (listNumber < 1 || listNumber > gamesListMemory.size()) {
            throw new Exception("That game number does not exist..");
        }
        return gamesListMemory
                .stream()
                .toList()
                .get(listNumber - 1);
    }

    private LogoutResult playGame(String[] params, String authToken) throws Exception {
        if (params.length != 2) {
            return new LogoutResult(
                    false,
                    "Expected: play <game number> <WHITE|BLACK>\n");
        }
        int listNumber;
        try {
            listNumber = Integer.parseInt(params[0]);
        } catch (NumberFormatException ex) {
            return new LogoutResult(
                    false,
                    "Game number must be a number.\n");
        }
        String playerColor = params[1].toUpperCase();
        if (!playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
            return new LogoutResult(
                    false,
                    "Color must be WHITE or BLACK.\n");
        }
        GameData selectedGame = getGameFromListNumber(listNumber);
        server.joinGame(authToken, playerColor, selectedGame.gameID());
        return new LogoutResult(
                false,
                "Joined " + selectedGame.gameName() + " as " + playerColor + ".\n");
    }

    private LogoutResult listGames(String authToken) throws Exception {
        var games = server.listGames(authToken);
        gamesListMemory = games;

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
            case "play" -> playGame(params, authToken);
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