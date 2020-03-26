package pt.isel.ls.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Table {

    private final int columnCount;
    private final List<List<String>> rows = new LinkedList<>();
    private final List<Integer> maxSizes;

    /**
     * Creates a new table with the specified columns
     * @param columnNames columns of the table
     */
    public Table(String... columnNames) {
        rows.add(Arrays.asList(columnNames));

        this.columnCount = columnNames.length;
        this.maxSizes = new ArrayList<>();

        for (int i = 0; i < columnCount; i++) {
            maxSizes.add(columnNames[i].length());
        }
    }

    /**
     * Adds a new row to the table
     * @param values values of the row
     * @throws IllegalArgumentException if row size mismatches the table size
     */
    public void addTableRow(String... values) throws IllegalArgumentException {
        if (values.length != columnCount) {
            throw new IllegalArgumentException("Row size is different from Table size!");
        }

        rows.add(Arrays.asList(values));
        checkMaxColumnSize(values);
    }

    public int getHorizontalSize() {
        int totalCharacters = 0;
        for (int max : maxSizes) {
            totalCharacters += max;
        }

        return totalCharacters + 3 * columnCount;
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getColumnSize(int col) {
        return maxSizes.get(col);
    }

    public List<String> getRow(int row) {
        return Collections.unmodifiableList(rows.get(row));
    }

    private void checkMaxColumnSize(String... newItems) {
        for (int i = 0; i < columnCount; ++i) {
            String columnItem = newItems[i];
            if (columnItem.length() > maxSizes.get(i)) {
                maxSizes.set(i, columnItem.length());
            }
        }
    }

}
