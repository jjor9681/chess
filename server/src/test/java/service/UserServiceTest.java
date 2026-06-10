package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setup() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        userService = new UserService(
                userDAO,
                authDAO
        );
    }

    @Test
    public void registerPositive() throws Exception {
        UserData userData = new UserData("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        AuthData authData = userService.register(userData);
        assertNotNull(authData);
        assertEquals("Jonas", authData.username());
        assertNotNull(authData.authToken());
    }
    @Test
    public void registerNegative() throws Exception {
        UserData userData = new UserData("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        userService.register(userData);
        assertThrows(AlreadyTakenException.class, () -> userService.register(userData));
    }

    @Test
    public void loginPositive() throws Exception {
        UserData userData = new UserData("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        userService.register(userData);
        AuthData authData = userService.login(userData);
        assertNotNull(authData);
        assertEquals("Jonas", authData.username());
        assertNotNull(authData.authToken());
    }
    @Test
    public void loginNegative() throws Exception {
        UserData realUser = new UserData("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        userService.register(realUser);
        UserData wrongPassword = new UserData("Jonas", "Bruh", "jonas@outlook.com");
        assertThrows(UnauthorizedException.class, () -> userService.login(wrongPassword));
    }

    @Test
    public void logoutPositive() throws Exception {
        UserData userData = new UserData("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        AuthData authData = userService.register(userData);
        assertDoesNotThrow(() -> userService.logout(authData.authToken()));
    }
    @Test
    public void logoutNegative() {
        assertThrows(UnauthorizedException.class, () -> userService.logout("SomebodyElse'sAuthToken"));
    }

}