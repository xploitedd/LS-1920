package pt.isel.ls.router.request.parameter;

import pt.isel.ls.exceptions.parameter.AmbiguousParameterException;
import pt.isel.ls.exceptions.parameter.ParameterNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParameterValueList implements Iterable<Parameter> {

    private final List<Parameter> parameters = new ArrayList<>();
    private final String parameterName;

    public ParameterValueList(String parameterName) {
        this.parameterName = parameterName;
    }

    public void add(Parameter parameter) {
        parameters.add(parameter);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Parameter getUnique() {
        if (parameters.size() == 0) {
            throw new ParameterNotFoundException(parameterName);
        }

        if (parameters.size() > 1) {
            throw new AmbiguousParameterException(parameterName);
        }

        return parameters.get(0);
    }

    public <T> List<T> map(Function<Parameter, T> mapper) {
        return parameters.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Parameter> iterator() {
        return parameters.iterator();
    }

}
