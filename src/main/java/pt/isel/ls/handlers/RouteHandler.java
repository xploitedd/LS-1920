package pt.isel.ls.handlers;

import pt.isel.ls.router.Request;
import pt.isel.ls.router.Response;

public interface RouteHandler {

    Response execute(Request request);

}
