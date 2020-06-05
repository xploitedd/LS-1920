package pt.isel.ls.router.request.validator;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.exceptions.parameter.ParameterNotFoundException;
import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.error.ParameterError;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Validator {

    private final HashMap<String, ValidatorInfo<?>> mappings = new HashMap<>();
    private final HashMap<String, LinkedList<Consumer<Object>>> filters = new HashMap<>();

    public <T> Validator addMapping(String parameterName, ValidatorFunction<T> mapper) {
        return addMapping(parameterName, mapper, false);
    }

    public <T> Validator addMapping(String parameterName, ValidatorFunction<T> mapper, boolean optional) {
        ValidatorInfo<T> info = new ValidatorInfo<>(mapper, optional);
        mappings.put(parameterName, info);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> Validator addFilter(String parameterName, Consumer<T> filter, Class<T> clazz) {
        LinkedList<Consumer<Object>> list = filters.computeIfAbsent(parameterName, k -> new LinkedList<>());
        Consumer<T> np = test -> {
            Class<?> other = test.getClass();
            if (!other.equals(clazz)) {
                throw new ValidatorException("Error testing a filter of a parameter!");
            }

            filter.accept(test);
        };

        list.add((Consumer<Object>) np);
        return this;
    }

    public ValidatorResult validate(RouteRequest request) {
        HashMap<String, Object> results = new HashMap<>();
        ParameterError errors = new ParameterError();

        for (String parameter : mappings.keySet()) {
            ValidatorInfo<?> info = mappings.get(parameter);
            List<Consumer<Object>> filter = filters.get(parameter);
            try {
                try {
                    Object res = info.func.apply(request.getParameter(parameter));
                    boolean passedFilters = true;
                    if (filter != null) {
                        for (Consumer<Object> pred : filter) {
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
