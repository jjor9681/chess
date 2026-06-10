package handling;
import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.UserService;
import service.UnauthorizedException;
import java.util.Map;

public class Logout implements Handler {

    private final UserService userService;
    private final Gson gson = new Gson();

    public Logout(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            userService.logout(authToken);
            ctx.status(200);
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