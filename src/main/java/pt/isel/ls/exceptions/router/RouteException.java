package pt.isel.ls.exceptions.router;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.router.StatusCode;

public class RouteException extends AppException {

    public RouteException(String message) {
        super(message);
    }

    public RouteException(String message, StatusCode statusCode) {
        super(message, statusCode);
    }

}
