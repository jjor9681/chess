package handling;

import io.javalin.http.Handler;
import service.DeleteService;
import io.javalin.http.Context;

import java.util.Map;

public class Clear implements Handler {

    private final DeleteService deleteService;

    public Clear(DeleteService deleteService) {
        this.deleteService = deleteService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            deleteService.delete();
            ctx.status(200);
        }
        catch (Exception ex) {
            ctx.status(500);
            ctx.json(Map.of("message", ex.getMessage())); // I can do this instead of making a whole new class.
        }
    }
}