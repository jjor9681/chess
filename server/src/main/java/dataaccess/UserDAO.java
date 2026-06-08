package dataaccess;

// Needs access to model package and user data.
import model.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;

    void getUser(String username) throws  DataAccessException;
}
