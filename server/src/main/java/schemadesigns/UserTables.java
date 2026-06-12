// Table setup for user data.
package schemadesigns;

public class UserTables {

    public static final String CREATE_STATEMENT = """
        CREATE TABLE IF NOT EXISTS user (
            username VARCHAR(255) PRIMARY KEY,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL
        )
        """;
}