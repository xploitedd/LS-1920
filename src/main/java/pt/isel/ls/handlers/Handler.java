package pt.isel.ls.handlers;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;

public interface Handler {

    /**
     * Handles a specific request
     * @param request request to be handled
     * @return the response for this request
     */
    HandlerResponse execute(RouteRequest request);

}
