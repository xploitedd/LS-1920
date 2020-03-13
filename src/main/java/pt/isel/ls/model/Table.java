package pt.isel.ls.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Table {

    private final List<List<String>> rows = new LinkedList<>();

    public Table(String... columnNames) {
        rows.add(Arrays.asList(columnNames));
    }

    public void addTableRow(String... values) throws IllegalArgumentException {
        if (values.length != countColumns()) {
            throw new IllegalArgumentException("Row size is different from Table size!");
        }

        rows.add(Arrays.asList(values));
    }

    public int countColumns() {
        return rows.get(0).size();
    }

    public int countRows() {
        return rows.size();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        // print header
        for (int i = 0; i < rows.size(); ++i) {
            List<String> row = rows.get(i);
            for (int j = 0; j < row.size(); j++) {
                sb.append(row.get(i));
                if (i + 1 != row.size()) {
                    sb.append("\t ");
                }
            }
        }

        return sb.toString();
    }

}
