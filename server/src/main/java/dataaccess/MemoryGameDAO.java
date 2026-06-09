package dataaccess;

import model.GameData;

// I used collection earlier so I will include it here as well along with HashMap.
import java.util.Collection;
import java.util.HashMap;

// The memory files are basically a copy from pet shop.
public class MemoryGameDAO implements GameDAO{

    // I suspect I need to map gameId to game data.
    private final HashMap<Integer,GameData> allGames = new HashMap<>(); // The documentation says game data is integers, so that is matched here.

    @Override
    public void createGame(GameData game) throws DataAccessException {
        allGames.put(game.gameID(),game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return allGames.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        // Hashmaps store keys and values, but I am only looking for game data objects.
        // Stack overflow says that .values() can be used to do what I need here.
        return allGames.values();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        allGames.put(game.gameID(),game); // The same as createGame.
    }

    @Override
    public void clear() throws DataAccessException {
        allGames.clear();
    }
}
