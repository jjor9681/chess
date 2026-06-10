package handling;
import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.GameData;
import service.GameService;
import service.UnauthorizedException;
import java.util.Collection;
import java.util.Map;

public class ListGames implements Handler {

    private final GameService gameService;
    private final Gson gson = new Gson();

    public ListGames(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            Collection<GameData> games = gameService.listGames(authToken);
            ctx.status(200);
            ctx.result(gson.toJson(Map.of("games", games)));
        }
        catch (UnauthorizedException ex) {
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", ex.getMessage())));
        }
        catch (Exception ex) {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", ex.getMessage())));
        }
    }
}