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
        assertEquals("bob", authData.username());
        assertNotNull(authData.authToken());
    }
    // okay the
    @Test
    public void registerNegative() throws Exception {
        UserData userData = new UserData("Jonas", "GreenEggsAndHam", "jonas@outlook.com");
        userService.register(userData);
        assertThrows(AlreadyTakenException.class, () -> userService.register(userData));
    }
}