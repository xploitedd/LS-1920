package pt.isel.ls.router.request.parameter;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.exceptions.parameter.ParameterNotFoundException;
import pt.isel.ls.router.StatusCode;

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

    public String getErrorString() {
        StringBuilder sb = new StringBuilder();
        for (String parameter : errors.getErrors().keySet()) {
            String error = errors.getErrors().get(parameter);
            sb.append(parameter).append(": ").append(error).append(", ");
        }

        // substring to remove the last ", "
        return sb.substring(0, sb.length() - 2);
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
