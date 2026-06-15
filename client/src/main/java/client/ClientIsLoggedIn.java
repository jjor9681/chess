package client;

import model.GameData;
import serverfacade.ServerFacade;
import ui.GameBuilder;

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
                    false,
                    null,
                    null,
                    "Expected: create <game name>\n");
        }
        String gameName = String.join(" ", params);
        server.createGame(authToken, gameName);
        return new LogoutResult(
                false,
                false,
                null,
                null,
                "Created game: " + gameName + "\n");
    }

    private GameData getGameFromListNumber(int listNumber) throws Exception {
        if (gamesListMemory == null || gamesListMemory.isEmpty()) {
            throw new Exception("You need to list the games first.");
        }
        if (listNumber < 1 || listNumber > gamesListMemory.size()) {
            throw new Exception("That game number does not exist..");
        }
        return gamesListMemory
                .stream()
                .toList()
                .get(listNumber - 1);
    }

    // just adding this so that the player sees none instead of null.
    private String displayPlayer(String username) {
        if (username == null) {
            return "none";
        }

        return username;
    }

    private LogoutResult playGame(String[] params, String authToken) throws Exception {
        if (params.length != 2) {
            return new LogoutResult(
                    false,
                    false,
                    null,
                    null,
                    "Expected: play <game number> <WHITE|BLACK>\n");
        }
        int listNumber;
        try {
            listNumber = Integer.parseInt(params[0]);
        } catch (NumberFormatException ex) {
            return new LogoutResult(
                    false,
                    false,
                    null,
                    null,
                    "Game number must be a number.\n");
        }
        String playerColor = params[1].toUpperCase();
        if (!playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
            return new LogoutResult(
                    false,
                    false,
                    null,
                    null,
                    "Color must be WHITE or BLACK.\n");
        }
        GameData selectedGame = getGameFromListNumber(listNumber);
        server.joinGame(authToken, playerColor, selectedGame.gameID());

        GameBuilder board = new GameBuilder();

        if (playerColor.equals("WHITE")) {
            return new LogoutResult(
                    false,
                    true,
                    selectedGame.gameID(),
                    playerColor,
                    "Joined " + selectedGame.gameName() + " as WHITE.\n\n"
                            + board.buildWhiteBoard());
        }

        return new LogoutResult(
                false,
                true,
                selectedGame.gameID(),
                playerColor,
                "Joined " + selectedGame.gameName() + " as BLACK.\n\n"
                        + board.buildBlackBoard());
    }

    private LogoutResult listGames(String authToken) throws Exception {
        var games = server.listGames(authToken);
        gamesListMemory = games;

        if (games.isEmpty()) {
            return new LogoutResult(
                    false,
                    false,
                    null,
                    null,
                    "No games available.\n");
        }
        StringBuilder result = new StringBuilder();
        int number = 1;
        for (var game : games) {
            result.append(number)
                    .append(". ")
                    .append(game.gameName())
                    .append(" | White: ")
                    .append(displayPlayer(game.whiteUsername()))
                    .append(" | Black: ")
                    .append(displayPlayer(game.blackUsername()))
                    .append("\n");
            number++;
        }
        return new LogoutResult(
                false,
                false,
                null,
                null,
                result.toString());
    }

    private LogoutResult observeGame(String[] params) throws Exception {
        if (params.length != 1) {
            return new LogoutResult(
                    false,
                    false,
                    null,
                    null,
                    "Expected: observe <game number>\n");
        }
        int listNumber;
        try {
            listNumber = Integer.parseInt(params[0]);
        } catch (NumberFormatException ex) {
            return new LogoutResult(
                    false,
                    false,
                    null,
                    null,
                    "Game number must be a number.\n");
        }
        GameData selectedGame = getGameFromListNumber(listNumber);

        GameBuilder board = new GameBuilder();

        return new LogoutResult(
                false,
                true,
                selectedGame.gameID(),
                "WHITE",
                "Observing " + selectedGame.gameName() + ".\n\n"
                        + board.buildWhiteBoard());
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
            case "observe" -> observeGame(params);
            default -> new LogoutResult(
                    false,
                    false,
                    null,
                    null,
                    "Unknown command. Type help.\n");
        };
    }

    private LogoutResult logout(String authToken) throws Exception {
        server.logout(authToken);

        return new LogoutResult(
                true,
                false,
                null,
                null,
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
            boolean enteredGame,
            Integer gameID,
            String perspective,
            String message
    ) {}
}