package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.router.RouteException;

public interface RouteHandler {

    RouteResponse execute(RouteRequest request) throws RouteException;

}
