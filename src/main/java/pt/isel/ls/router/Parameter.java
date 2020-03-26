package pt.isel.ls.router;

public class Parameter {

    private final String value;

    public Parameter(String value) {
        this.value = value;
    }

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
