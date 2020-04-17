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

import static pt.isel.ls.dsl.Dsl.*;

public class TableView extends View {

    private final Table table;

    public TableView(Table table) {
        this.table = table;
    }

    @Override
    protected void renderText(PrintWriter writer) {
        writer.println(table);
    }

    @Override
    protected Node getHtmlBody() {
        ArrayList<TableRowElement> rows = new ArrayList<>();
        // map table header
        rows.add(tr(mapToTableText(table.getHeader(), Dsl::th)));

        // map table data rows
        table.getRowsStream().forEach(r ->
                rows.add(tr(
                        mapToTableText(r, Dsl::td)
                ))
        );

        return table(
                rows.toArray(TableRowElement[]::new)
        );
    }

    private <T> TableText[] mapToTableText(Stream<T> stream, Function<String, TableText> mapper) {
        return stream.map(cell -> mapper.apply(cell.toString()))
                .toArray(TableText[]::new);
    }
}
