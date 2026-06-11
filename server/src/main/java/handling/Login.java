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

    // had to make a helper method so that I could avoid duplicate code.
    private void serverError(Context ctx, Exception ex, int status) {
        ctx.status(status);
        ctx.json(gson.toJson(Map.of("message", ex.getMessage())));
    }

    @Override
    public void handle(Context ctx) {
        try {// get username and password so i can check if they exist.
            UserData userData = gson.fromJson(ctx.body(), UserData.class);
            AuthData authData = userService.login(userData);
            ctx.status(200);
            ctx.result(gson.toJson(authData));
        }
        catch (BadRequestException ex) {
            serverError(ctx, ex, 400);
        }
        catch (UnauthorizedException ex) {
            serverError(ctx, ex, 401);
        }
        catch (Exception ex) {
            serverError(ctx, ex, 500);
        }
    }
}