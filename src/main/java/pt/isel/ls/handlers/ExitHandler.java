package pt.isel.ls.handlers;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.ExitView;

public class ExitHandler implements RouteHandler {

    private static final String DESCRIPTION = "Exits the application";

    /**
     * Exits the application
     * @param request The route request
     * @return returns a RouteResponse with a ExitView for the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        return new HandlerResponse(new ExitView());
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

}
