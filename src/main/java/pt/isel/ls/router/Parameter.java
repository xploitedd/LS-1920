package pt.isel.ls.router;

public class Parameter {

    private final String value;

    /**
     * Creates a new Parameter from a String
     * @param value value of the parameter
     */
    public Parameter(String value) {
        this.value = value;
    }

    /**
     * Tries to convert this parameter to an int
     * @return an integer representation of this parameter
     * @throws RouteException if there was an error converting to int
     */
    public int toInt() throws RouteException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RouteException("Invalid conversion to integer. Value: " + value);
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
