package pt.isel.ls.router.request.validator;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.exceptions.parameter.ParameterNotFoundException;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.router.response.error.ParameterErrors;

import java.util.HashMap;
import java.util.Optional;

public class ValidatorResult {

    private final HashMap<String, Object> parameterResults;
    private final ParameterErrors errors;

    public ValidatorResult(HashMap<String, Object> parameterResults, ParameterErrors errors) {
        this.parameterResults = parameterResults;
        this.errors = errors;
    }

    public boolean hasErrors() {
        return errors.getErrors().size() > 0;
    }

    public ParameterErrors getErrors() {
        return errors;
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameterValue(String parameter) {
        Object obj = parameterResults.get(parameter);
        if (obj == null) {
            throw new ParameterNotFoundException(parameter);
        }

        try {
            return (T) obj;
        } catch (ClassCastException e) {
            throw new AppException(e.getMessage(), StatusCode.INTERNAL_SEVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptionalParameter(String parameter) {
        Object obj = parameterResults.get(parameter);
        if (obj == null) {
            return Optional.empty();
        }

        try {
            return Optional.of((T) obj);
        } catch (ClassCastException e) {
            throw new AppException(e.getMessage(), StatusCode.INTERNAL_SEVER_ERROR);
        }
    }

}
