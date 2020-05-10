package pt.isel.ls.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

import pt.isel.ls.model.dsl.Dsl;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.table.TableElement;
import pt.isel.ls.model.dsl.elements.table.TableRowElement;
import pt.isel.ls.model.dsl.text.table.TableText;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.Router;

import static pt.isel.ls.model.dsl.Dsl.table;
import static pt.isel.ls.model.dsl.Dsl.tr;

public class TableView extends View {

    private final Table table;
    private final ArrayList<TableRowElement> htmlRows;
    private TableElement cache;

    public TableView(String tableName, Table table) {
        super(tableName);
        this.table = table;
        this.htmlRows = new ArrayList<>();
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.println(table);
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        if (cache == null) {
            // map table header
            addRow(mapToTableText(table.getHeader(), Dsl::th));
            // map table data rows
            table.getRowsStream().forEach(r -> addRow(mapToTableText(r, Dsl::td)));

            cache = table(
                    htmlRows.toArray(TableRowElement[]::new)
            ).attr("border", "1");
        }

        // this cache is not shared across different requests so there's
        // no racing condition
        return cache;
    }

    private void addRow(TableText... rowText) {
        htmlRows.add(tr(rowText));
    }

    private <T> TableText[] mapToTableText(Stream<T> stream, Function<String, TableText> mapper) {
        return stream.map(cell -> mapper.apply(cell.toString())
                .<TableText>attr("style", "padding:5px;"))
                .toArray(TableText[]::new);
    }

    public Table getTable() {
        return table;
    }

}
