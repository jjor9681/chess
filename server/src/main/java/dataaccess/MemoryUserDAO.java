package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

        String username = user.username();

        users.put(username, user); // put() is used with hashmaps for data entry.
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username); // Hashmap will return null so I can use this for registration.
    }
}
