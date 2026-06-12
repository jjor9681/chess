// chess game object table
package schemadesigns;

public class GameTables {

    public static final String CREATE_STATEMENT = """
        CREATE TABLE IF NOT EXISTS game (
            gameID INT PRIMARY KEY,
            whiteUsername VARCHAR(255),
            blackUsername VARCHAR(255),
            gameName VARCHAR(255) NOT NULL,
            gameState TEXT NOT NULL
        )
        """;
}