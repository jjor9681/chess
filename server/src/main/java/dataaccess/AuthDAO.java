package dataaccess;

import model.AuthData;
// just throws exceptions whenever soemthing breaks.
public interface AuthDAO {
    void createAuth(AuthData auth) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth (String authToken) throws DataAccessException;

    void clear() throws DataAccessException;
}
