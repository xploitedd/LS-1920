package pt.isel.ls.router.request.parameter;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.exceptions.parameter.ParameterNotFoundException;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.router.request.RouteRequest;

import java.util.HashMap;
import java.util.function.Function;

public class Validator {

    private final HashMap<String, ValidatorInfo> rules = new HashMap<>();

    public Validator addRule(String parameterName, ValidatorFunction mapper) {
        return addRule(parameterName, mapper, false);
    }

    public Validator addRule(String parameterName, ValidatorFunction mapper, boolean optional) {
        ValidatorInfo info = new ValidatorInfo(mapper, optional);
        rules.put(parameterName, info);
        return this;
    }

    public ValidatorResult validate(RouteRequest request) {
        HashMap<String, Object> results = new HashMap<>();
        ParameterErrors errors = new ParameterErrors();

        for (String parameter : rules.keySet()) {
            ValidatorInfo info = rules.get(parameter);
            try {
                try {
                    Object res = info.func.apply(request.getParameter(parameter));
                    results.put(parameter, res);
                } catch (ParameterNotFoundException e) {
                    if (!info.optional) {
                        throw e;
                    }
                }
            } catch (AppException e) {
                // if the error isn't because of the parameters then pass
                // the error
                if (e.getStatusCode() != StatusCode.BAD_REQUEST) {
                    throw e;
                }

                errors.addError(parameter, e.getMessage());
            }
        }

        return new ValidatorResult(results, errors);
    }

    private static class ValidatorInfo {

        public final ValidatorFunction func;
        public final boolean optional;

        public ValidatorInfo(ValidatorFunction func, boolean optional) {
            this.func = func;
            this.optional = optional;
        }

    }

    public interface ValidatorFunction extends Function<ParameterValueList, Object> {

    }

}
