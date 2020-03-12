package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

public interface RouteHandler {

    RouteResponse execute(RouteRequest request);

}
