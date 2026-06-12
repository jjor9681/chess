// I am getting ragebaited by the non-public getConnection()
// method in DatabaseManager, so I'm going to make a wrapper haha.

package dataaccess;

import java.sql.Connection;

public class DatabaseManagerWrapper {

    public static Connection getConnection()
            throws DataAccessException {

        return DatabaseManager.getConnection();
    }
}