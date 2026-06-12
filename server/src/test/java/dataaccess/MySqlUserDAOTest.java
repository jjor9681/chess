package dataaccess;

import dataaccess.sqldataaccess.MySqlUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class MySqlUserDAOTest {

    private UserDAO userDAO;
    @BeforeEach
    public void setup() throws Exception {
        DatabaseInitializer.initialize();
        userDAO = new MySqlUserDAO();
        userDAO.clear();
    }

    @Test
    public void createUserPositive() throws Exception {
        UserData user = new UserData(
                        "Jonas",
                        "greeneggsandham",
                        "jonas@outlook.com");
        userDAO.createUser(user);
        UserData result = userDAO.getUser("Jonas");
        assertNotNull(result);
    }

    @Test
    public void createUserNegative() throws Exception {

        UserData user = new UserData(
                        "Jonas",
                        "bruh",
                        "jonas@outlook.com");
        userDAO.createUser(user);
        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    public void getUserPositive() throws Exception {

        UserData user = new UserData(
                        "Jonas",
                        "YEET",
                        "jonas@hotmail.com");
        userDAO.createUser(user);
        assertNotNull(userDAO.getUser("Jonas"));
    }

    @Test
    public void getUserNegative() throws Exception {

        assertNull(userDAO.getUser("RonaldMcDonald"));
    }

    @Test
    public void clearPositive() throws Exception {
        UserData user = new UserData(
                        "Jonas",
                        "sigma",
                        "jonas@icloud.com");

        userDAO.createUser(user);
        userDAO.clear();
        assertNull(userDAO.getUser("Jonas"));
    }
}
