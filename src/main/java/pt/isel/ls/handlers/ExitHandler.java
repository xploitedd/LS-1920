package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.ExitView;

public class ExitHandler implements RouteHandler {

    @Override
    public RouteResponse execute(RouteRequest request) {
        return new RouteResponse(new ExitView());
    }

}
