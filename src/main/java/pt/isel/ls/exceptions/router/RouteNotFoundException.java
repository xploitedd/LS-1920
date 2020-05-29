package pt.isel.ls.exceptions.router;

import pt.isel.ls.router.StatusCode;
import pt.isel.ls.router.request.Path;

public class RouteNotFoundException extends RouteException {

    public RouteNotFoundException(Path path) {
        super("The route " + path + " was not found!", StatusCode.NOT_FOUND);
    }

}
