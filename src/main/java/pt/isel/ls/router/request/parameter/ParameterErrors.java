package pt.isel.ls.router.request.parameter;

import java.util.HashMap;
import java.util.Map;

public class ParameterErrors {

    private final HashMap<String, String> errors = new HashMap<>();

    public void addError(String parameterName, String error) {
        errors.put(parameterName, error);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

}
