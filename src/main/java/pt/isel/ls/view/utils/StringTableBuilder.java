package pt.isel.ls.view.utils;

import pt.isel.ls.model.Table;

import java.util.List;

public class StringTableBuilder<T> extends TableBuilder<T, String, Object, String> {

    public StringTableBuilder(Iterable<T> content) {
        super(content);
    }

    @Override
    public String build() {
        String[] columnNames = header.toArray(String[]::new);
        Table table = new Table(columnNames);
        for (List<Object> cols : rows) {
            Object[] colsArr = cols.toArray();
            table.addTableRow((Object[]) colsArr);
        }

        return table.toString();
    }

}
