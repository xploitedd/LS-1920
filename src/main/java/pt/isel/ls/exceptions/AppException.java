package pt.isel.ls.exceptions;

public class AppException extends RuntimeException {

    public AppException(String message) {
        super(message, null, false, false);
    }

}
