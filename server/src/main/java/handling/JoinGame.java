package handling;
import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.NewPlayer;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.GameService;
import service.UnauthorizedException;
import java.util.Map;

public class JoinGame implements Handler {

    private final GameService gameService;
    private final Gson gson = new Gson();

    public JoinGame(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            NewPlayer newPlayer = gson.fromJson(ctx.body(), NewPlayer.class);
            gameService.joinGame(authToken, newPlayer.playerColor(), newPlayer.gameID());
            ctx.status(200);
        }
        catch (BadRequestException ex) {
            ctx.status(400);
            ctx.json(Map.of("message", ex.getMessage()));
        }
        catch (UnauthorizedException ex) {
            ctx.status(401);
            ctx.json(Map.of("message", ex.getMessage()));
        }
        catch (AlreadyTakenException ex) {
            ctx.status(403);
            ctx.json(Map.of("message", ex.getMessage()));
        }
        catch (Exception ex) {
            ctx.status(500);
            ctx.json(Map.of("message", ex.getMessage()));
        }
    }
}