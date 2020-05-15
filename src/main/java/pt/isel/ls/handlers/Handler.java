package pt.isel.ls.handlers;

import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;

public interface Handler {

    HandlerResponse execute(Router router, RouteRequest request);

}
