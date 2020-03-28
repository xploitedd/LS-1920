package pt.isel.ls.view.console;

import java.io.PrintWriter;
import pt.isel.ls.model.Table;

public class TableView implements View {

    private Table table;

    public TableView(Table table) {
        this.table = table;
    }

    @Override
    public void render(PrintWriter writer) {
        writer.println(table);
    }

}
