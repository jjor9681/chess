package handling;
import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.AuthData;
import model.UserData;
import service.UserService;
import service.BadRequestException;
import service.AlreadyTakenException;
import java.util.Map;

public class Register implements Handler {

    private final UserService userService;
    private final Gson gson = new Gson();

    public Register(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            // which username and password should be registered
            UserData userData = gson.fromJson(ctx.body(), UserData.class);
            AuthData authData = userService.register(userData);
            ctx.status(200);
            ctx.result(gson.toJson(authData));
        }
        catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message", ex.getMessage())));
        }
        catch (AlreadyTakenException ex) {
            // can't steal usernames. hit em with a 403
            ctx.status(403);
            ctx.result(gson.toJson(Map.of("message", ex.getMessage())));
        }
        catch (Exception ex) {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", ex.getMessage())));
        }
    }
}