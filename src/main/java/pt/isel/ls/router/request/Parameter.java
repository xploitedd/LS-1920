package pt.isel.ls.router.request;

import pt.isel.ls.exceptions.router.RouteException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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
     */
    public int toInt() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RouteException("Invalid conversion to integer. Value: " + value);
        }
    }

    /**
     * Tries to convert this parameter to a long
     * @return a long representation of this parameter
     */
    public long toLong() {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new RouteException("Invalid conversion to long. Value: " + value);
        }
    }

    public long toTime() {
        try {
            return toLong();
        } catch (RouteException e) {
            try {
                return Timestamp.valueOf(LocalDateTime.parse(value)).getTime();
            } catch (DateTimeParseException parseException) {
                throw new RouteException("Invalid time: " + parseException.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
