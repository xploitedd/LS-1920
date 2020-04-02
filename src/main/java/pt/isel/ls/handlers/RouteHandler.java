package pt.isel.ls.handlers;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.router.response.RouteException;

public interface RouteHandler {

    RouteResponse execute(RouteRequest request) throws RouteException;

}
