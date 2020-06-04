package pt.isel.ls.exceptions.parameter;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.router.StatusCode;

public class ParameterException extends AppException {

    public ParameterException(String message) {
        super(message, StatusCode.BAD_REQUEST);
    }

}
