package handling;
import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.GameData;
import service.BadRequestException;
import service.GameService;
import service.UnauthorizedException;
import java.util.Map;

public class CreateGame implements Handler {

    private final GameService gameService;
    private final Gson gson = new Gson();

    public CreateGame(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            // get auth token and parse body so i know what game they wanna make
            String authToken = ctx.header("Authorization");
            GameData gameData = gson.fromJson(ctx.body(), GameData.class);
            int gameID = gameService.createGame(authToken, gameData.gameName());
            // send it all back in json if it worked.
            ctx.status(200);
            ctx.result(gson.toJson(Map.of("gameID", gameID)));
        } // if it breaks, then hopefully debugging will go easier with these
        catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message", ex.getMessage())));
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