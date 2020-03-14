package pt.isel.ls.view.console;

import pt.isel.ls.model.Table;

public class TableView implements View {

    Table table;

    public TableView(Table table) {
        this.table = table;
    }

    @Override
    public void render() {
        System.out.println(table);
    }

}
