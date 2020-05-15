package pt.isel.ls.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.table.TableElement;
import pt.isel.ls.model.dsl.elements.table.TableRowElement;
import pt.isel.ls.model.dsl.text.TextNode;
import pt.isel.ls.model.dsl.text.table.TableText;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Path;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.p;
import static pt.isel.ls.model.dsl.Dsl.table;
import static pt.isel.ls.model.dsl.Dsl.td;
import static pt.isel.ls.model.dsl.Dsl.th;
import static pt.isel.ls.model.dsl.Dsl.tr;

public class TableView extends View {

    protected final Table table;
    protected final ArrayList<TableRowElement> htmlRows;
    protected TableElement cache;

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
            addRow(mapToTableText(table.getHeader(), h -> th(h.toString())));
            // map table data rows
            addRows();

            cache = table(
                    htmlRows.toArray(TableRowElement[]::new)
            ).attr("border", "1");
        }

        // this cache is not shared across different requests so there's
        // no racing condition
        return cache;
    }

    private void addRows() {
        table.getRowsStream().forEach(r -> addRow(mapToTableText(r, TableView::parseData)));
    }

    private void addRow(TableText... rowText) {
        htmlRows.add(tr(rowText));
    }

    private static TableText[] mapToTableText(Stream<Object> stream, Function<Object, TableText> mapper) {
        return stream.map(cell -> mapper.apply(cell)
                .<TableText>attr("style", "padding:5px;"))
                .toArray(TableText[]::new);
    }

    private static TableText parseData(Object obj) {
        if (obj instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>) obj;
            LinkedList<TextNode> res = new LinkedList<>();
            for (Object o : iterable) {
                res.add(getPath(o.toString()));
            }

            return td(res.toString());
        }

        return td(getPath(obj.toString()));
    }

    private static TextNode getPath(String path) {
        // check if it's a path
        Optional<Path> opt = Path.of(path);
        if (opt.isPresent()) {
            return a(path, path);
        } else {
            return p(path);
        }
    }

    public Table getTable() {
        return table;
    }

}
