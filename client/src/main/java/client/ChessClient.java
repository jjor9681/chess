package client;

import client.gameplay.GameplayClient;
import client.gameplay.Result;
import serverfacade.ServerFacade;

import java.util.Scanner;

// basically my giant repl controller.
public class ChessClient {

    private final ServerFacade server;
    private final ClientNotLoggedIn clientNotLoggedIn;
    private final ClientIsLoggedIn clientIsLoggedIn;
    private final int port;

    private GameplayClient gameplayClient;

    private SignedInStatus status = SignedInStatus.SIGNEDOUT;
    private String authToken;
    private String username;

    public ChessClient(int port) {
        this.port = port;
        server = new ServerFacade(port);
        clientNotLoggedIn = new ClientNotLoggedIn(server);
        clientIsLoggedIn = new ClientIsLoggedIn(server);
    }

    public void run() {
        System.out.println("Welcome to Chess!");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);

                if (!result.equals("quit")) {
                    System.out.print(result);
                }
            } catch (Exception ex) {
                System.out.print(ex.getMessage() + "\n");
            }
        }

        System.out.println("Goodbye!");
    }

    public String eval(String input) throws Exception {
        if (input.trim().isEmpty()) {
            return help();
        }

        String command = getCommand(input);
        String[] params = getParams(input);

        if (command.equals("help")) {
            return help();
        }

        if (command.equals("quit")) {
            return "quit";
        }

        if (status == SignedInStatus.SIGNEDOUT) {
            return handleSignedOut(command, params);
        }

        if (status == SignedInStatus.INGAME) {
            return handleInGame(command, params);
        }

        return handleSignedIn(command, params);
    }

    private String handleSignedOut(
            String command,
            String[] params) throws Exception {

        ClientNotLoggedIn.LoginResult result =
                clientNotLoggedIn.eval(
                        command,
                        params);

        if (result.loggedIn()) {
            authToken = result.authToken();
            username = result.username();
            status = SignedInStatus.SIGNEDIN;
        }

        return result.message();
    }

    private String handleSignedIn(
            String command,
            String[] params) throws Exception {

        ClientIsLoggedIn.LogoutResult result =
                clientIsLoggedIn.eval(
                        command,
                        params,
                        authToken);

        if (result.loggedOut()) {
            authToken = null;
            username = null;
            status = SignedInStatus.SIGNEDOUT;
            return result.message();
        }

        if (result.enteredGame()) {
            gameplayClient =
                    new GameplayClient(
                            "http://localhost:" + port,
                            authToken,
                            result.gameID(),
                            result.perspective());

            status = SignedInStatus.INGAME;
        }

        return result.message();
    }

    private String handleInGame(
            String command,
            String[] params) throws Exception {

        Result result =
                gameplayClient.eval(
                        command,
                        params);

        if (result.leftGame()) {
            gameplayClient = null;
            status = SignedInStatus.SIGNEDIN;
        }

        return result.message();
    }

    private String getCommand(String input) {
        return input
                .trim()
                .split("\\s+")[0]
                .toLowerCase();
    }

    private String[] getParams(String input) {
        String trimmedInput =
                input.trim();

        int firstSpace =
                trimmedInput.indexOf(" ");

        if (firstSpace == -1) {
            return new String[0];
        }

        String paramsText =
                trimmedInput
                        .substring(firstSpace + 1)
                        .trim();

        if (paramsText.isEmpty()) {
            return new String[0];
        }

        return paramsText.split("\\s+");
    }

    private String help() {
        if (status == SignedInStatus.SIGNEDOUT) {
            return clientNotLoggedIn.help();
        }

        if (status == SignedInStatus.INGAME) {
            return gameplayClient.help();
        }

        return clientIsLoggedIn.help();
    }

    private void printPrompt() {
        if (status == SignedInStatus.SIGNEDOUT) {
            System.out.print("\n[LOGGED OUT] >>> ");
        } else if (status == SignedInStatus.INGAME) {
            System.out.print("\n[" + username + " | GAME] >>> ");
        } else {
            System.out.print("\n[" + username + "] >>> ");
        }
    }
}