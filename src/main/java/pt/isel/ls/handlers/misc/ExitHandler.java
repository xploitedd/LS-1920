package pt.isel.ls.handlers.misc;

import pt.isel.ls.app.http.HttpPool;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.misc.ExitView;

public final class ExitHandler extends RouteHandler {

    private final HttpPool httpPool;

    public ExitHandler(HttpPool httpPool) {
        super(
                Method.EXIT,
                "/",
                "Exits the application"
        );

        this.httpPool = httpPool;
    }

    /**
     * Exits the application
     * @param request The route request
     * @return returns a RouteResponse with a ExitView for the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        httpPool.terminate();
        return new HandlerResponse(new ExitView());
    }

}
