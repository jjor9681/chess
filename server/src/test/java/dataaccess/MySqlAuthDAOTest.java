package dataaccess;

import dataaccess.sqldataaccess.MySqlAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class MySqlAuthDAOTest {

    private AuthDAO authDAO;
    @BeforeEach
    public void setup() throws Exception {
        DatabaseInitializer.initialize();
        authDAO = new MySqlAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createAuthPositive() throws Exception {
        AuthData auth = new AuthData("token", "Jonas");
        authDAO.createAuth(auth);
        AuthData result = authDAO.getAuth("token");
        assertNotNull(result);
        assertEquals("Jonas", result.username());
    }

    @Test
    public void createAuthNegative() throws Exception {
        AuthData auth = new AuthData("token", "Jonas");
        authDAO.createAuth(auth);
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(auth));
    }

    @Test
    public void getAuthPositive() throws Exception {
        AuthData auth = new AuthData("token", "Jonas");
        authDAO.createAuth(auth);
        AuthData result = authDAO.getAuth("token");
        assertNotNull(result);
        assertEquals("token", result.authToken());
        assertEquals("Jonas", result.username());
    }

    @Test
    public void getAuthNegative() throws Exception {
        assertNull(authDAO.getAuth("hackerToken"));
    }

    @Test
    public void deleteAuthPositive() throws Exception {
        AuthData auth = new AuthData("token", "Jonas");
        authDAO.createAuth(auth);
        authDAO.deleteAuth("token");
        assertNull(authDAO.getAuth("token"));
    }

    @Test
    public void deleteAuthNegative() {
        assertDoesNotThrow(() -> authDAO.deleteAuth("hackerToken"));
    }

    @Test
    public void clearPositive() throws Exception {
        AuthData auth = new AuthData("token", "Jonas");
        authDAO.createAuth(auth);
        authDAO.clear();
        assertNull(authDAO.getAuth("token"));
    }
}