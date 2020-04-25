package pt.isel.ls.dsl;

import java.util.Objects;

public class Attribute {

    private final String name;
    private final String value;

    /**
     * Creates a new HTML Attribute
     * @param name Attribute name
     * @param value Attribute value
     */
    Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Get the name of this attribute
     * @return name of the attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value of this attribute
     * @return value of the attribute
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Attribute attribute = (Attribute) o;
        return name.equals(attribute.name)
                && value.equals(attribute.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return name + "=\"" + value + "\"";
    }

}
