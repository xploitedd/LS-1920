package pt.isel.ls.router.response.error;

import pt.isel.ls.exceptions.AppException;

public class HandlerError extends Error<String> {

    public static HandlerError fromException(AppException e) {
        HandlerError err = new HandlerError();
        err.addError(e.getMessage());
        return err;
    }

}
