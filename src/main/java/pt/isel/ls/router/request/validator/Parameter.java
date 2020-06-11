package pt.isel.ls.router.request.validator;

import pt.isel.ls.exceptions.parameter.ValidatorException;

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
            throw new ValidatorException("Invalid Parameter Type");
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
            throw new ValidatorException("Invalid Parameter Type");
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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                default:
                    sb.append(c);
            }
        }

        return sb.toString();
    }

}