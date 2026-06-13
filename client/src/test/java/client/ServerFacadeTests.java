package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {



    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        // Okay i should probably have an initializer for the facade here.
        facade = new ServerFacade(port);

    }

    // probably need to nuke everything between tests
    @BeforeEach
    public void clearDatabase() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerPositive() throws Exception {

        AuthData authData =
                facade.register(
                        "Jonas",
                        "GreenEggsAndHam",
                        "jonas@outlook.com");

        assertNotNull(authData);
        assertEquals("Jonas", authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    public void registerNegative() throws Exception {
        facade.register(
                "Jonas",
                "GreenEggsAndHam",
                "jonas@outlook.com");

        assertThrows(Exception.class, () -> facade.register(
                        "Jonas",
                        "GreenEggsAndHam",
                        "jonas@outlook.com"));
    }

    @Test
    public void loginPositive() throws Exception {
        facade.register(
                "Jonas",
                "GreenEggsAndHam",
                "jonas@outlook.com");

        AuthData authData =
                facade.login(
                        "Jonas",
                        "GreenEggsAndHam");
        assertNotNull(authData);
        assertEquals("Jonas", authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    public void loginNegative() {
        assertThrows(Exception.class, () -> facade.login(
                        "Jonas",
                        "susPassword"));
    }

    @Test
    public void logoutPositive() throws Exception {
        AuthData authData =
                facade.register(
                        "Jonas",
                        "GreenEggsAndHam",
                        "jonas@outlook.com");
        assertDoesNotThrow(() -> facade.logout(authData.authToken()));
    }

    @Test
    public void logoutNegative() {
        assertThrows(Exception.class, () -> facade.logout("fakeAuthToken"));
    }

    @Test
    public void createGamePositive() throws Exception {
        AuthData authData = facade.register("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        int gameID = facade.createGame(authData.authToken(), "200_EloGame");
        assertTrue(gameID > 0);
    }

    @Test
    public void createGameNegative() {
        assertThrows(
                Exception.class, () -> facade.createGame("trashAuthToken", "200_EloGame"));
    }

    @Test
    public void listGamesPositive() throws Exception {
        AuthData authData = facade.register("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        facade.createGame(authData.authToken(), "3000 Elo Game");
        facade.createGame(authData.authToken(), "200 Elo Game");
        var games = facade.listGames(authData.authToken());
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesNegative() {
        assertThrows(Exception.class, () -> facade.listGames("fakeAuthToken"));
    }

    @Test
    public void joinGamePositive() throws Exception {
        AuthData authData = facade.register("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        int gameID = facade.createGame(authData.authToken(), "200_EloGame");
        assertDoesNotThrow(() -> facade.joinGame(authData.authToken(), "WHITE", gameID));
    }

    @Test
    public void joinGameNegative() throws Exception {
        AuthData authData = facade.register("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        int gameID = facade.createGame(authData.authToken(), "200_EloGame");
        facade.joinGame(authData.authToken(), "WHITE", gameID);
        assertThrows(Exception.class, () -> facade.joinGame(authData.authToken(), "WHITE", gameID));
    }

    @Test
    public void clearPositive() throws Exception {
        AuthData authData = facade.register("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        facade.createGame(authData.authToken(), "200_EloGame");
        facade.clear();
        assertThrows(Exception.class, () -> facade.listGames(authData.authToken()));
    }
}
