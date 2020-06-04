package pt.isel.ls.exceptions.parameter;

public class NotEnoughParametersException extends ParameterException {

    public NotEnoughParametersException(int expected, int actual) {
        super("Not enough parameters! Expected: " + expected + " Actual: " + actual);
    }

}
