package pt.isel.ls.exceptions.router;

import pt.isel.ls.router.StatusCode;

public class NotEnoughParametersException extends RouteException {

    public NotEnoughParametersException(int expected, int actual) {
        super("Not enough parameters! Expected: " + expected + " Actual: " + actual, StatusCode.BAD_REQUEST);
    }

}
