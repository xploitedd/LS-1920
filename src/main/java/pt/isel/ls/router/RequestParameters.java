package pt.isel.ls.router;

import java.util.HashMap;
import java.util.Optional;

public final class RequestParameters<E> {

    private final HashMap<String, E> parameters;

    public RequestParameters(HashMap<String, E> parameters) {
        this.parameters = parameters;
    }

    public E getParameter(String parameterName) throws ParameterNotFoundException {
        return Optional.ofNullable(parameters.get(parameterName))
                .orElseThrow(() -> new ParameterNotFoundException(parameterName));
    }

    public Optional<E> getOptionalParameter(String parameterName) {
        return Optional.ofNullable(parameters.get(parameterName));
    }

    public static class ParameterNotFoundException extends Exception {

        public ParameterNotFoundException(String parameterName) {
            super("Parameter " + parameterName + " not found!");
        }

    }

}
