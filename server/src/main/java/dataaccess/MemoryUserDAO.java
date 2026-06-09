package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

        String username = user.username();

        users.put(username, user); // put() is used with hashmaps for data entry.
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username); // Hashmap will return null if a user is missing so I can use this for registration.
    }
}
