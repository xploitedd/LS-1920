package pt.isel.ls.handlers.validators;

import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.router.request.validator.Validator;
import pt.isel.ls.router.request.validator.ValidatorResult;

import java.util.Optional;

public abstract class AbstractValidator {

    private final ValidatorResult result;

    public AbstractValidator(Validator validator) {
        this.result = validator.validate();
    }

    public ValidatorResult getResult() {
        return result;
    }

    public boolean hasErrors() {
        return result.hasErrors();
    }

    private void checkErrors() {
        if (result.hasErrors()) {
            throw new ValidatorException("Unhandled Errors Exception");
        }
    }

    protected <T> T getParameter(String parameterName) {
        checkErrors();
        return result.getParameterValue(parameterName);
    }

    protected <T> Optional<T> getOptionalParameter(String parameterName) {
        checkErrors();
        return result.getOptionalParameter(parameterName);
    }

}
