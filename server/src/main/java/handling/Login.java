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
            UserData userData = gson.fromJson(ctx.body(), UserData.class); // I can't get away from gson here. Nothing on stack overflow provides any better way to do it.
            AuthData authData = userService.login(userData);
            ctx.status(200);
            ctx.json(authData);
        }
        catch (BadRequestException ex) {
            ctx.status(400);
            ctx.json(Map.of("message", ex.getMessage()));
        }
        catch (UnauthorizedException ex) {
            ctx.status(401);
            ctx.json(Map.of("message", ex.getMessage()));
        }
        catch (Exception ex) {
            ctx.status(500);
            ctx.json(Map.of("message", ex.getMessage()));
        }
    }
}