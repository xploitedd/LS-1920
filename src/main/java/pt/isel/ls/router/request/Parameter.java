package pt.isel.ls.router.request;

import pt.isel.ls.exceptions.router.RouteException;

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

    /**
     * Tries to convert this parameter to a long
     * @return a long representation of this parameter
     * @throws RouteException if there was an error converting to long
     */
    public long toLong() throws RouteException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new RouteException("Invalid conversion to long. Value: " + value);
        }
    }

    public boolean isEmpty() {
        return value.isEmpty() || value.isBlank();
    }

    @Override
    public String toString() {
        return value;
    }

}
