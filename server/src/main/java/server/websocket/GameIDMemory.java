// Store all games by gameID.

package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameIDMemory {

    private final ConcurrentHashMap<Integer, Set<Session>> gameSessions =
            new ConcurrentHashMap<>();

    public void add(
            int gameID,
            Session session) {

        gameSessions
                .computeIfAbsent(
                        gameID,
                        id -> ConcurrentHashMap.newKeySet())
                .add(session);
    }

    public void remove(
            int gameID,
            Session session) {

        Set<Session> sessions =
                gameSessions.get(gameID);

        if (sessions == null) {
            return;
        }

        sessions.remove(session);

        if (sessions.isEmpty()) {
            gameSessions.remove(gameID);
        }
    }

    public Set<Session> getConnections(int gameID) {

        Set<Session> sessions =
                gameSessions.get(gameID);

        if (sessions == null) {
            return Set.of();
        }

        return sessions;
    }
}