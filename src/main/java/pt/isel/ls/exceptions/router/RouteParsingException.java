package pt.isel.ls.exceptions.router;

import pt.isel.ls.router.StatusCode;

public class RouteParsingException extends RouteException {

    public RouteParsingException(String message) {
        super("Error while parsing the route: " + message, StatusCode.BAD_REQUEST);
    }

}
