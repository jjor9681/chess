package dataaccess;

import schemadesigns.AuthTables;
import schemadesigns.GameTables;
import schemadesigns.UserTables;

import java.sql.SQLException;

public class DatabaseInitializer {

    private static final String[] CREATE_STATEMENTS = {
            UserTables.CREATE_STATEMENT,
            AuthTables.CREATE_STATEMENT,
            GameTables.CREATE_STATEMENT
    };

    // Not package private so server.java can call this and starts
    // a domino effect to start up the server.
    public static void initialize() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String statement : CREATE_STATEMENTS) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to initialize database", ex);
        }
    }
}