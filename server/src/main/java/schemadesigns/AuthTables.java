// table setup for login info/authorization
package schemadesigns;

public class AuthTables {

    public static final String CREATE_STATEMENT = """
        CREATE TABLE IF NOT EXISTS auth (
            authToken VARCHAR(255) PRIMARY KEY,
            username VARCHAR(255) NOT NULL
        )
        """;
}