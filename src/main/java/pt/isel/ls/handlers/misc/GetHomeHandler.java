package pt.isel.ls.handlers.misc;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.misc.HomeView;

public class GetHomeHandler extends RouteHandler {

    public GetHomeHandler() {
        super(
                Method.GET,
                "/",
                "Home route of the application"
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        return new HandlerResponse(new HomeView());
    }

}
