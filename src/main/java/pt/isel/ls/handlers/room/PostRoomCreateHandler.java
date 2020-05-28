package pt.isel.ls.handlers.room;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;

public class PostRoomCreateHandler extends RouteHandler {

    public PostRoomCreateHandler(Method method, String template, String description) {
        super(method, template, description);
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        return null;
    }
}
