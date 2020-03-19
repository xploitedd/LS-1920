package pt.isel.ls.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Table implements Model {

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

    private void checkMaxColumnSize(String... newItems) {
        for (int i = 0; i < columnCount; ++i) {
            String columnItem = newItems[i];
            if (columnItem.length() > maxSizes.get(i)) {
                maxSizes.set(i, columnItem.length());
            }
        }
    }

    private void appendRow(int row, StringBuffer sb) {
        for (int i = 0; i < columnCount; i++) {
            int colMax = maxSizes.get(i);
            sb.append(String.format("%-" + colMax + "s | ", rows.get(row).get(i)));
        }
    }

    private int countHorizontalSize() {
        int totalCharacters = 0;
        for (int max : maxSizes) {
            totalCharacters += max;
        }

        return totalCharacters + 3 * columnCount;
    }

    @Override
    public String toString() {
        int size = countHorizontalSize();
        String separator = "-".repeat(size);
        StringBuffer sb = new StringBuffer(separator);
        sb.append("\n");
        // print header
        appendRow(0, sb);

        // print header separator
        // +3*columnCount because of the " | " printed above
        sb.append("\n").append("=".repeat(size));

        // print table rows
        for (int i = 1; i < rows.size(); ++i) {
            sb.append("\n");
            appendRow(i, sb);
        }

        return sb.append("\n").append(separator).toString();
    }

}
