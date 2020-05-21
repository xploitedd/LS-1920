package pt.isel.ls.utils;

import pt.isel.ls.model.dsl.elements.table.TableElement;
import pt.isel.ls.model.dsl.elements.table.TableRowElement;
import pt.isel.ls.model.dsl.text.table.TableDataText;
import pt.isel.ls.model.dsl.text.table.TableHeaderText;
import pt.isel.ls.model.dsl.text.table.TableText;

import java.util.ArrayList;
import java.util.List;

import static pt.isel.ls.model.dsl.Dsl.table;
import static pt.isel.ls.model.dsl.Dsl.tr;

public class HtmlTableBuilder<T> extends TableBuilder<T, TableHeaderText, TableDataText, TableElement> {

    public HtmlTableBuilder(Iterable<T> content) {
        super(content);
    }


    public TableElement build() {
        ArrayList<TableRowElement> htmlRows = new ArrayList<>(rows.size() + 1);
        htmlRows.add(tr(header.toArray(TableText[]::new)));
        for (List<TableDataText> col : rows) {
            htmlRows.add(tr(col.toArray(TableText[]::new)));
        }

        return table(
                htmlRows.toArray(TableRowElement[]::new)
        ).attr("border", "1");
    }

}
