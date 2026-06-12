package dataaccess.sqldataaccess;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManagerWrapper;
import dataaccess.UserDAO;
import model.UserData;

public class MySqlUserDAO implements UserDAO {

    @Override
    public void clear() throws DataAccessException {

        String statement = "TRUNCATE TABLE user";

        // using a wrapper to get around having to have all my sql files crammed into dataaccess.
        try (var conn = DatabaseManagerWrapper.getConnection();
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
    public UserData getUser(String username)
            throws DataAccessException {
        String statement =
                """
                SELECT username, password, email
                FROM user
                WHERE username = ?
                """;
        try (var conn = DatabaseManagerWrapper.getConnection();
             var preparedStatement =
                     conn.prepareStatement(statement)) {
            preparedStatement.setString(1, username);
            // petshop called this variable rs, and i believe that means results.
            try (var results = preparedStatement.executeQuery()) {
                if (results.next()) {
                    return new UserData(
                            results.getString("username"),
                            results.getString("password"),
                            results.getString("email"));
                }
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException(
                    "Unable to retrieve user", e);
        }
    }
}