package pt.isel.ls.exceptions.parameter;

import pt.isel.ls.router.request.validator.ValidatorResult;

public class ValidatorException extends ParameterException {

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(ValidatorResult result) {
        super(result.getErrors().toString());
    }

}
