package pt.isel.ls.handlers.misc;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.ExitView;

public final class ExitHandler extends RouteHandler {

    public ExitHandler() {
        super(
                Method.EXIT,
                "/",
                "Exits the application"
        );
    }

    /**
     * Exits the application
     * @param request The route request
     * @return returns a RouteResponse with a ExitView for the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        return new HandlerResponse(new ExitView());
    }

}
