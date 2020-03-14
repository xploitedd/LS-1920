package pt.isel.ls.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Table {

    private final StringBuffer stringBuffer = new StringBuffer();
    private final List<List<String>> rows = new LinkedList<>();

    public Table(String... columnNames) {
        rows.add(Arrays.asList(columnNames));
        appendRowToBuffer(columnNames);
    }

    public void addTableRow(String... values) throws IllegalArgumentException {
        if (values.length != countColumns()) {
            throw new IllegalArgumentException("Row size is different from Table size!");
        }

        rows.add(Arrays.asList(values));
        appendRowToBuffer(values);
    }

    public int countColumns() {
        return rows.get(0).size();
    }

    public int countRows() {
        return rows.size();
    }

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
