package pt.isel.ls.exceptions;

public class ParameterNotFoundException extends AppException {

    public ParameterNotFoundException(String parameterName) {
        super("Parameter " + parameterName + " not found!");
    }

}
