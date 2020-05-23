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

    /**
     * Tries to convert this parameter to a long
     * This parameter must be a valid time in one of the following formats:
     *      - a unix millisecond timestamp
     *      - ISO_LOCAL_DATE_TIME, as specified in LocalDateTime
     * @return a long representation of the time
     */
    public long toTime() {
        try {
            // ISO_LOCAL_DATE_TIME is the preferred representation
            return Timestamp.valueOf(LocalDateTime.parse(value)).getTime();
        } catch (DateTimeParseException e) {
            return toLong();
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
