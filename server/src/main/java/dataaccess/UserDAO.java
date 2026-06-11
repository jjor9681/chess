package dataaccess;

// Needs access to model package and user data.
import model.UserData;
// just throws exceptions whenever soemthing breaks.
public interface UserDAO {
    void clear() throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws  DataAccessException;
}
