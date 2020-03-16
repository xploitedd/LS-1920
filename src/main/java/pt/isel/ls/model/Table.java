package pt.isel.ls.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Table {

    private final StringBuffer stringBuffer = new StringBuffer();
    private final List<List<String>> rows = new LinkedList<>();

    /**
     * Creates a new table with the specified columns
     * @param columnNames columns of the table
     */
    public Table(String... columnNames) {
        rows.add(Arrays.asList(columnNames));
        appendRowToBuffer(columnNames);
    }

    /**
     * Adds a new row to the table
     * @param values values of the row
     * @throws IllegalArgumentException if row size mismatches the table size
     */
    public void addTableRow(String... values) throws IllegalArgumentException {
        if (values.length != countColumns()) {
            throw new IllegalArgumentException("Row size is different from Table size!");
        }

        rows.add(Arrays.asList(values));
        appendRowToBuffer(values);
    }

    /**
     * Retrieves the column count
     * @return column count
     */
    public int countColumns() {
        return rows.get(0).size();
    }

    /**
     * Retrieves the row count
     * @return row count
     */
    public int countRows() {
        return rows.size();
    }

    /**
     * Appends the specified row to the string buffer
     * The string buffer is used to obtain a string representation
     * of the table
     * @param values row values
     */
    private void appendRowToBuffer(String... values) {
        for (int i = 0; i < values.length; i++) {
            stringBuffer.append(values[i]);
            if (i + 1 != values.length) {
                stringBuffer.append("\t ");
            }
        }
    }

    @Override
    public String toString() {
        return stringBuffer.toString();
    }

}
