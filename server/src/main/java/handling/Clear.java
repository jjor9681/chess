package handling;

import io.javalin.http.Handler;
import service.DeleteService;
import io.javalin.http.Context;
import com.google.gson.Gson;

import java.util.Map;

public class Clear implements Handler {

    private final DeleteService deleteService;
    // Okay I need gson here to format my errors. final so that the pointer doesn't change.
    private final Gson gson = new Gson();

    public Clear(DeleteService deleteService) {
        this.deleteService = deleteService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            // just wipes everything. 200 means it worked.
            deleteService.delete();
            ctx.status(200);
        }
        catch (Exception ex) {
            // If something crashes, just wrap the error message in JSON. Fancy writing if I do say so myself.
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", ex.getMessage())));
        }
    }
}