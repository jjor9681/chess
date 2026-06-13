package serverfacade;

import model.AuthData;
import model.GameData;
import model.NewPlayer;
import model.UserData;

import java.util.Collection;

public class ServerFacade {

    private final HttpTranslator http;

    public ServerFacade(int port) {
        String serverUrl = "http://localhost:" + port;
        http = new HttpTranslator(serverUrl);
    }

    public AuthData register(String username, String password, String email) throws Exception {
        UserData userData = new UserData(username, password, email);

        return http.post(
                "/user",
                userData,
                null,
                AuthData.class);
    }

    public AuthData login(String username, String password) throws Exception {
        UserData userData = new UserData(username, password, null);

        return http.post(
                "/session",
                userData,
                null,
                AuthData.class);
    }

    public void logout(String authToken) throws Exception {
        http.delete(
                "/session",
                null,
                authToken,
                null);
    }

    public int createGame(String authToken, String gameName) throws Exception {
        GameData gameData =
                new GameData(
                        0,
                        null,
                        null,
                        gameName,
                        null);

        CreateGameDataHolder response =
                http.post(
                        "/game",
                        gameData,
                        authToken,
                        CreateGameDataHolder.class);

        return response.gameID();
    }

    public Collection<GameData> listGames(String authToken) throws Exception {
        ListGamesDataHolder response =
                http.get(
                        "/game",
                        authToken,
                        ListGamesDataHolder.class);

        return response.games();
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws Exception {
        NewPlayer newPlayer =
                new NewPlayer(
                        playerColor,
                        gameID);

        http.put(
                "/game",
                newPlayer,
                authToken,
                null);
    }

    public void clear() throws Exception {
        http.delete(
                "/db",
                null,
                null,
                null);
    }
}