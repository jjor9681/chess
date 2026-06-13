package client;

import serverfacade.ServerFacade;
import java.util.Arrays;
import java.util.Scanner;

// basically my giant repl controller.
public class ChessClient {

    private final ServerFacade server;
    private final ClientNotLoggedIn clientNotLoggedIn;
    private final ClientIsLoggedIn clientIsLoggedIn;

    private SignedInStatus status = SignedInStatus.SIGNEDOUT;
    private String authToken;
    private String username;

    public ChessClient(int port) {
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
                System.out.print("Error: " + ex.getMessage() + "\n");
            }
        }
        System.out.println("Goodbye!");
    }
    public String eval(String input) throws Exception {
        if (input.trim().isEmpty()) {
            return help();
        }
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (command.equals("help")) {
            return help();
        }
        if (command.equals("quit")) {
            return "quit";
        }
        if (status == SignedInStatus.SIGNEDOUT) {
            ClientNotLoggedIn.LoginResult result =
                    clientNotLoggedIn.eval(command, params);
            if (result.loggedIn()) {
                authToken = result.authToken();
                username = result.username();
                status = SignedInStatus.SIGNEDIN;
            }
            return result.message();
        }

        ClientIsLoggedIn.LogoutResult result =
                clientIsLoggedIn.eval(command, params, authToken);
        if (result.loggedOut()) {
            authToken = null;
            username = null;
            status = SignedInStatus.SIGNEDOUT;
        }
        return result.message();
    }
    private String help() {
        if (status == SignedInStatus.SIGNEDOUT) {
            return clientNotLoggedIn.help();
        }
        return clientIsLoggedIn.help();
    }
    private void printPrompt() {
        if (status == SignedInStatus.SIGNEDOUT) {
            System.out.print("\n[LOGGED OUT] >>> ");
        } else {
            System.out.print("\n[" + username + "] >>> ");
        }
    }
}