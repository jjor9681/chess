package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{

    // Create another hashmap with same properties as MemUserDao
    private final HashMap<String,AuthData> authsList = new HashMap<>();

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

        String authToken = auth.authToken();
        authsList.put(authToken, auth);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authsList.get(authToken); // HashMap is way better here. Should be O(1) search time I believe.
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authsList.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        authsList.clear();
    }
}
