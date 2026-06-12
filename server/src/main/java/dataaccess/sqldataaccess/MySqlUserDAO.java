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
        // also petshop is ultra abbreviating things. I'll go with "preparedStatement" instead of ps
        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.executeUpdate();

        } catch (Exception ex) {
            throw new DataAccessException(
                    "Unable to clear users", ex);
        }
    }

    @Override
    public void createUser(UserData user)
            throws DataAccessException {
        String statement =
                """
                INSERT INTO user
                (username, password, email)
                VALUES (?, ?, ?)
                """;
        // This is the only way this can be done. I bet everyone did this.
        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps =
                     conn.prepareStatement(statement)) {
            ps.setString(
                    1,
                    user.username());
            ps.setString(
                    2,
                    user.password());
            ps.setString(
                    3,
                    user.email());
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException(
                    "Can't create user",
                    ex);
        }
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
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, username);
            // petshop called this variable rs, and i believe that means results.
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new UserData(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"));
                }
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException(
                    "Unable to retrieve user", e);
        }
    }
}