package pt.isel.ls.router.request.validator;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.exceptions.parameter.ParameterNotFoundException;
import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.error.ParameterErrors;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Validator {

    private final HashMap<String, ValidatorInfo<?>> mappings = new HashMap<>();
    private final HashMap<String, LinkedList<Consumer<Object>>> filters = new HashMap<>();
    private final RouteRequest request;

    public Validator(RouteRequest request) {
        this.request = request;
    }

    public <T> Validator addMapping(String parameterName, ValidatorFunction<T> mapper) {
        return addMapping(parameterName, mapper, false);
    }

    public <T> Validator addMapping(String parameterName, ValidatorFunction<T> mapper, boolean optional) {
        ValidatorInfo<T> info = new ValidatorInfo<>(mapper, optional);
        mappings.put(parameterName, info);
        return this;
    }

    public <T> Validator addFilter(String parameterName, Predicate<T> filter, Class<T> clazz) {
        return addFilter(parameterName, filter, clazz, parameterName + " is invalid!");
    }


    @SuppressWarnings("unchecked")
    public <T> Validator addFilter(String parameterName, Predicate<T> filter, Class<T> clazz, String errorMessage) {
        LinkedList<Consumer<Object>> list = filters.computeIfAbsent(parameterName, k -> new LinkedList<>());
        Consumer<T> np = test -> {
            if (test.getClass() != clazz) {
                throw new ValidatorException("Error validating field!");
            }

            if (!filter.test(test)) {
                throw new ValidatorException(errorMessage);
            }
        };

        list.add((Consumer<Object>) np);
        return this;
    }

    public ValidatorResult validate() {
        HashMap<String, Object> results = new HashMap<>();
        ParameterErrors errors = new ParameterErrors(request);

        for (String parameter : mappings.keySet()) {
            ValidatorInfo<?> info = mappings.get(parameter);
            List<Consumer<Object>> filter = filters.get(parameter);
            try {
                // this try checks whether this parameter was found
                try {
                    Object res = info.func.apply(request.getParameter(parameter));

                    boolean passedFilters = true;
                    if (filter != null) {
                        for (Consumer<Object> pred : filter) {
                            // we want to check all of the consumers, even if
                            // one of them is invalid...
                            try {
                                pred.accept(res);
                            } catch (AppException e) {
                                errors.addError(new AbstractMap.SimpleEntry<>(parameter, e.getMessage()));
                                passedFilters = false;
                            }
                        }
                    }

                    if (passedFilters) {
                        results.put(parameter, res);
                    }
                } catch (ParameterNotFoundException e) {
                    if (!info.optional) {
                        throw e;
                    }
                }
            } catch (AppException e) {
                errors.addError(new AbstractMap.SimpleEntry<>(parameter, e.getMessage()));
            }
        }

        return new ValidatorResult(results, errors);
    }

    private static class ValidatorInfo<T> {

        public final ValidatorFunction<T> func;
        public final boolean optional;

        public ValidatorInfo(ValidatorFunction<T> func, boolean optional) {
            this.func = func;
            this.optional = optional;
        }

    }

    public interface ValidatorFunction<T> extends Function<ParameterValueList, T> {

    }

}
