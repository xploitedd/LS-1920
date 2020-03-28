package pt.isel.ls.model;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * Get table horizontal size
     * Horizontal size is defined as the number of characters
     * necessary to represent the table horizontally
     * @return horizontal size
     */
    public int getHorizontalSize() {
        int totalCharacters = 0;
        for (int max : maxSizes) {
            totalCharacters += max;
        }

        return totalCharacters + 3 * columnCount;
    }

    /**
     * Updates the maximum column size for each column
     *
     * Maximum column size is defined as the maximum number
     * of characters needed to represent a column without
     * wrapping content
     * @param newItems the row that was added
     */
    private void checkMaxColumnSize(String... newItems) {
        for (int i = 0; i < columnCount; ++i) {
            String columnItem = newItems[i];
            if (columnItem.length() > maxSizes.get(i)) {
                maxSizes.set(i, columnItem.length());
            }
        }
    }

    /**
     * Appends a row to a specified StringBuffer
     * @param row row to be appended
     * @param sb StringBuffer where to append the row
     */
    private void appendRow(int row, StringBuffer sb) {
        List<String> rows = this.rows.get(row);
        int columnCount = this.rows.get(0).size();
        for (int i = 0; i < columnCount; i++) {
            int colMax = maxSizes.get(i);
            sb.append(String.format("%-" + colMax + "s | ", rows.get(i)));
        }
    }

    @Override
    public String toString() {
        int horizontalSize = getHorizontalSize();
        String separator = "-".repeat(horizontalSize);
        StringBuffer sb = new StringBuffer(separator);
        sb.append("\n");
        // print header
        appendRow(0, sb);

        // print header separator
        sb.append("\n").append("=".repeat(horizontalSize));

        // print table rows
        for (int i = 1; i < rows.size(); ++i) {
            sb.append("\n");
            appendRow(i, sb);
        }

        return sb.append("\n").append(separator).toString();
    }

}
