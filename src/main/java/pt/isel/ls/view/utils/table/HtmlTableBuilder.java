package pt.isel.ls.view.utils.table;

import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.elements.table.TableRowElement;
import pt.isel.ls.model.dsl.text.table.TableDataText;
import pt.isel.ls.model.dsl.text.table.TableHeaderText;
import pt.isel.ls.model.dsl.text.table.TableText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.table;
import static pt.isel.ls.model.dsl.Dsl.td;
import static pt.isel.ls.model.dsl.Dsl.th;
import static pt.isel.ls.model.dsl.Dsl.tr;

public class HtmlTableBuilder<T> extends TableBuilder<T, TableHeaderText, TableDataText, Element> {

    public HtmlTableBuilder(Iterable<T> content) {
        super(content);
    }

    public HtmlTableBuilder<T> withColumn(String columnName, Function<T, Object> mapper) {
        super.withColumn(th(columnName), f -> td(mapper.apply(f)));
        return this;
    }

    public Element build() {
        ArrayList<TableRowElement> htmlRows = new ArrayList<>(rows.size() + 1);
        htmlRows.add(tr(header.toArray(TableText[]::new)));
        for (List<TableDataText> col : rows) {
            htmlRows.add(tr(col.toArray(TableText[]::new)));
        }

        return div(table(
                htmlRows.toArray(TableRowElement[]::new)
        ).attr("border", "1"));
    }

}
