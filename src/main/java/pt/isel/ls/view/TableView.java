package pt.isel.ls.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import pt.isel.ls.dsl.Dsl;
import pt.isel.ls.dsl.Node;
import pt.isel.ls.dsl.elements.table.TableRowElement;
import pt.isel.ls.dsl.text.table.TableText;
import pt.isel.ls.model.Table;

import static pt.isel.ls.dsl.Dsl.table;
import static pt.isel.ls.dsl.Dsl.tr;

public class TableView extends View {

    private Table table;

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
        rows.add(tr(table.getHeader().map(Dsl::th).toArray(TableText[]::new)));
        table.getRowsStream().forEach(r ->
                rows.add(tr(r.map(Dsl::td).toArray(TableText[]::new))));

        return table(
                rows.toArray(TableRowElement[]::new)
        );
    }
}
