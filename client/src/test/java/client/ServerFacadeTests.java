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
        assertThrows(
                Exception.class,
                () -> facade.login(
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
}
