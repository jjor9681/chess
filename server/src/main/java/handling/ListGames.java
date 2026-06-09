package handling;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.GameData;
import service.GameService;
import service.UnauthorizedException;
import java.util.Collection;
import java.util.Map;

public class ListGames implements Handler {

    private final GameService gameService;

    public ListGames(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            Collection<GameData> games = gameService.listGames(authToken);
            ctx.status(200);
            ctx.json(games);
        }
        catch (UnauthorizedException ex) {
            ctx.status(401);
            ctx.json(Map.of("message", ex.getMessage()
            ));
        }
        catch (Exception ex) {
            ctx.status(500);
            ctx.json(Map.of("message", ex.getMessage()
            ));
        }
    }
}