package serverfacade;

import model.GameData;

import java.util.Collection;

public record ListGamesDataHolder(
        Collection<GameData> games
) {}