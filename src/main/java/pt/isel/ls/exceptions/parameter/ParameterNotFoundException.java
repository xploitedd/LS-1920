package pt.isel.ls.exceptions.parameter;

public class ParameterNotFoundException extends ParameterException {

    public ParameterNotFoundException(String parameterName) {
        super("Parameter " + parameterName + " not found!");
    }

}
