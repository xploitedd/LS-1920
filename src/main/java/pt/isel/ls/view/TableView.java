package pt.isel.ls.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

import pt.isel.ls.dsl.Dsl;
import pt.isel.ls.dsl.Node;
import pt.isel.ls.dsl.elements.table.TableRowElement;
import pt.isel.ls.dsl.text.table.TableText;
import pt.isel.ls.model.Table;

import static pt.isel.ls.dsl.Dsl.table;
import static pt.isel.ls.dsl.Dsl.tr;

public class TableView extends View {

    private final Table table;
    private final ArrayList<TableRowElement> htmlRows;

    public TableView(String tableName, Table table) {
        super(tableName);
        this.table = table;
        this.htmlRows = new ArrayList<>();
    }

    @Override
    protected void renderText(PrintWriter writer) {
        writer.println(table);
    }

    @Override
    protected Node getHtmlBody() {
        // map table header
        addRow(mapToTableText(table.getHeader(), Dsl::th));
        // map table data rows
        table.getRowsStream().forEach(r -> addRow(mapToTableText(r, Dsl::td)));

        return table(
                htmlRows.toArray(TableRowElement[]::new)
        ).addAttribute("border", "1");
    }

    private void addRow(TableText... rowText) {
        htmlRows.add(tr(rowText));
    }

    private <T> TableText[] mapToTableText(Stream<T> stream, Function<String, TableText> mapper) {
        return stream.map(cell -> mapper.apply(cell.toString())
                .<TableText>addAttribute("style", "padding:5px;"))
                .toArray(TableText[]::new);
    }
}
