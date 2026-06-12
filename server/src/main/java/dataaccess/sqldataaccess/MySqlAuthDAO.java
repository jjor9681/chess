package dataaccess.sqldataaccess;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManagerWrapper;
import model.AuthData;

public class MySqlAuthDAO implements AuthDAO {

    @Override
    public void createAuth(AuthData auth)
            throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken)
            throws DataAccessException {

        return null;
    }

    @Override
    public void deleteAuth(String authToken)
            throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

        String statement = "TRUNCATE TABLE auth";

        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.executeUpdate();

        } catch (Exception e) {
            throw new DataAccessException(
                    "Unable to clear auth table",
                    e);
        }
    }
}