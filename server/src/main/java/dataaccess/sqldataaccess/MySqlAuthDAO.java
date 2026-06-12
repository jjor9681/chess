package dataaccess.sqldataaccess;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManagerWrapper;
import model.AuthData;

public class MySqlAuthDAO implements AuthDAO {

    // basically a copy paste of createUser() but with different fields.
    @Override
    public void createAuth(AuthData auth)
            throws DataAccessException {
        String statement =
                """
                INSERT INTO auth
                (authToken, username)
                VALUES (?, ?)
                """;
        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(
                    1,
                    auth.authToken());
            ps.setString(
                    2,
                    auth.username());
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException(
                    "Unable to create auth",
                    ex);
        }
    }

    @Override
    public AuthData getAuth(String authToken)
            throws DataAccessException {
        String statement =
                """
                SELECT authToken, username
                FROM auth
                WHERE authToken = ?
                """;
        try (var conn = DatabaseManagerWrapper.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, authToken);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(
                            rs.getString("authToken"),
                            rs.getString("username")
                    );
                }
            }
            return null;
        } catch (Exception ex) {
            throw new DataAccessException(
                    "Unable to retrieve auth",
                    ex);
        }
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

        } catch (Exception ex) {
            throw new DataAccessException(
                    "Unable to clear auth table",
                    ex);
        }
    }
}