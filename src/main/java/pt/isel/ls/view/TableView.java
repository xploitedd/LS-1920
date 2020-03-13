package pt.isel.ls.view;

import pt.isel.ls.model.Table;

import java.util.Set;

public class TableView implements View {

    Table table;

    public TableView(Table table){
        this.table = table;
    }

    @Override
    public void render() {
    }
}
