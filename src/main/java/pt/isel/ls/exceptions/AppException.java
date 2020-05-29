package pt.isel.ls.exceptions;

import pt.isel.ls.router.StatusCode;

public class AppException extends RuntimeException {

    private final StatusCode statusCode;

    public AppException(String message) {
        this(message, StatusCode.INTERNAL_SEVER_ERROR);
    }

    public AppException(String message, StatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

}
