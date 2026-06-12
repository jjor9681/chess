package dataaccess.sqldataaccess;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import dataaccess.DatabaseManager;

public class MySqlUserDAO implements UserDAO {

    @Override
    public void clear() throws DataAccessException {

        String statement = "TRUNCATE TABLE user";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new DataAccessException(
                    "Unable to clear users", e);
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}