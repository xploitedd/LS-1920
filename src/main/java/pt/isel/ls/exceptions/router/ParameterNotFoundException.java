package pt.isel.ls.exceptions.router;

import pt.isel.ls.router.StatusCode;

public class ParameterNotFoundException extends RouteException {

    public ParameterNotFoundException(String parameterName) {
        super("Parameter " + parameterName + " not found!", StatusCode.NOT_FOUND);
    }

}
