package pt.isel.ls.router.response.error;

import pt.isel.ls.exceptions.AppException;

public class HandlerErrors extends Errors<String> {

    public static HandlerErrors fromException(AppException e) {
        HandlerErrors err = new HandlerErrors();
        err.addError(e.getMessage());
        return err;
    }

}
