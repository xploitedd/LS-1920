package pt.isel.ls.exceptions.router;

public class RouteParsingException extends RouteException {

    public RouteParsingException(String message) {
        super("Error while parsing the route: " + message);
    }

}
