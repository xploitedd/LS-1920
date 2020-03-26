package pt.isel.ls.view.console;

import java.io.PrintWriter;
import java.util.List;
import pt.isel.ls.model.Table;

public class TableView implements View {

    private Table table;

    public TableView(Table table) {
        this.table = table;
    }

    @Override
    public void render(PrintWriter writer) {
        int horizontalSize = table.getHorizontalSize();
        String separator = "-".repeat(horizontalSize);
        StringBuffer sb = new StringBuffer(separator);
        sb.append("\n");
        // print header
        appendRow(0, sb);

        // print header separator
        sb.append("\n").append("=".repeat(horizontalSize));

        // print table rows
        for (int i = 1; i < table.getRowCount(); ++i) {
            sb.append("\n");
            appendRow(i, sb);
        }

        writer.println(sb.append("\n").append(separator).toString());
    }

    private void appendRow(int row, StringBuffer sb) {
        List<String> rows = table.getRow(row);
        for (int i = 0; i < table.getColumnCount(); i++) {
            int colMax = table.getColumnSize(i);
            sb.append(String.format("%-" + colMax + "s | ", rows.get(i)));
        }
    }

}
