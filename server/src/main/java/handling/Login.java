package handling;
import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.AuthData;
import model.UserData;
import service.UserService;
import service.BadRequestException;
import service.UnauthorizedException;
import java.util.Map;

public class Login implements Handler {

    private final UserService userService;
    private final Gson gson = new Gson();

    public Login(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            UserData userData = gson.fromJson(ctx.body(), UserData.class);
            AuthData authData = userService.login(userData);
            ctx.status(200);
            ctx.result(gson.toJson(authData));
        }
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