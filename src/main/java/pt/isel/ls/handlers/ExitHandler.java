package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.console.ExitView;

public class ExitHandler implements RouteHandler {
    /**
     * Exits
     * @param request The route request
     * @return returns a RouteResponse with a ExitView for the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) {
        return new RouteResponse(new ExitView());
    }

}
