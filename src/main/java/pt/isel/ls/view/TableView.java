package pt.isel.ls.view;

import java.io.PrintWriter;
import pt.isel.ls.model.Table;

public class TableView extends View {

    private Table table;

    public TableView(Table table) {
        this.table = table;
    }

    @Override
    public void renderText(PrintWriter writer) {
        writer.println(table);
    }

}
