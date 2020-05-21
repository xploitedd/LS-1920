package pt.isel.ls.utils;

import pt.isel.ls.model.Table;

import java.util.List;

public class StringTableBuilder<T> extends TableBuilder<T, String, String, String> {

    public StringTableBuilder(Iterable<T> content) {
        super(content);
    }

    @Override
    public String build() {
        String[] columnNames = header.toArray(String[]::new);
        Table table = new Table(columnNames);
        for (List<String> cols : rows) {
            String[] colsArr = cols.toArray(String[]::new);
            table.addTableRow((Object[]) colsArr);
        }

        return table.toString();
    }

}
