package pt.isel.ls.exceptions.parameter;

public class AmbiguousParameterException extends ParameterException {

    public AmbiguousParameterException(String parameterName) {
        super("Ambiguous parameter value for parameter: " + parameterName);
    }

}
