package dataaccess;

import dataaccess.schema.AuthTable;
import dataaccess.schema.GameTable;
import dataaccess.schema.UserTable;

import java.sql.SQLException;

public class DatabaseInitializer {

    private static final String[] CREATE_STATEMENTS = {
            UserTable.CREATE_STATEMENT,
            AuthTable.CREATE_STATEMENT,
            GameTable.CREATE_STATEMENT
    };

    public static void initialize() throws DataAccessException {

        DatabaseManager.createDatabase();

        try (var conn = DatabaseManager.getConnection()) {

            for (String statement : CREATE_STATEMENTS) {

                try (var preparedStatement =
                             conn.prepareStatement(statement)) {

                    preparedStatement.executeUpdate();
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException(
                    "Unable to initialize database", ex);
        }
    }
}