package pt.isel.ls.handlers;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;

public interface RouteHandler {

    HandlerResponse execute(RouteRequest request) throws RouteException;

    default String getDescription() {
        return "No description available!";
    }

}
