package pt.isel.ls.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Table {

    private static final String COLUMN_SEPARATOR = "┃";
    private static final String TOP_BOTTOM_BORDER = "─";
    private static final String TABLE_DESCRIPTION_SEPARATOR = "━";

    private final int columnCount;
    private final List<List<Object>> rows = new LinkedList<>();
    private final List<Integer> maxSizes;

    /**
     * Creates a new table with the specified columns
     * @param columnNames columns of the table
     */
    public Table(String... columnNames) {
        LinkedList<Object> columns = new LinkedList<>(Arrays.asList(columnNames));
        rows.add(columns);

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
    public void addTableRow(Object... values) throws IllegalArgumentException {
        if (values.length != columnCount) {
            throw new IllegalArgumentException("Row size is different from Table size!");
        }

        rows.add(Arrays.asList(values));
        checkMaxColumnSize(values);
    }

    /**
     * Get a stream of the header values of this table
     * @return stream of the header
     */
    public Stream<Object> getHeader() {
        return rows.get(0).stream();
    }

    /**
     * Get a stream of rows of this table
     * excluding the header
     * @return stream of rows
     */
    public Stream<Stream<Object>> getRowsStream() {
        return rows.stream().skip(1).map(Collection::stream);
    }

    /**
     * Get the row count, excluding the header
     * @return row count
     */
    public int getRowCount() {
        return rows.size() - 1;
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

        return totalCharacters + 3 * columnCount - 2;
    }

    /**
     * Updates the maximum column size for each column
     *
     * Maximum column size is defined as the maximum number
     * of characters needed to represent a column without
     * wrapping content
     * @param newItems the row that was added
     */
    private void checkMaxColumnSize(Object... newItems) {
        for (int i = 0; i < columnCount; ++i) {
            String columnItem = newItems[i].toString();
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
        List<Object> columns = this.rows.get(row);
        for (int i = 0; i < columnCount; i++) {
            int colMax = maxSizes.get(i);
            String separator = i + 1 == columnCount ? "" : COLUMN_SEPARATOR + " ";
            sb.append(String.format("%-" + colMax + "s " + separator,
                    columns.get(i)));
        }
    }

    @Override
    public String toString() {
        int horizontalSize = getHorizontalSize();
        String separator = TOP_BOTTOM_BORDER.repeat(horizontalSize);
        StringBuffer sb = new StringBuffer(separator);
        sb.append("\n");
        // print header
        appendRow(0, sb);

        // print header separator
        sb.append("\n").append(TABLE_DESCRIPTION_SEPARATOR.repeat(horizontalSize));

        // print table rows
        for (int i = 1; i < rows.size(); ++i) {
            sb.append("\n");
            appendRow(i, sb);
        }

        return sb.append("\n").append(separator).toString();
    }

}
