package dataaccess.sqldataaccess;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManagerWrapper;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class MySqlGameDAO implements GameDAO {

    @Override
    public void createGame(GameData game)
            throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID)
            throws DataAccessException {

        return null;
    }

    @Override
    public Collection<GameData> listGames()
            throws DataAccessException {

        return null;
    }

    @Override
    public void updateGame(GameData game)
            throws DataAccessException {

    }

    @Override
    public void clear()
            throws DataAccessException {

    }
}