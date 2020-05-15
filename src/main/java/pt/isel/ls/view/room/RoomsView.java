package pt.isel.ls.view.room;

import pt.isel.ls.handlers.room.GetRoomSearchHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.TableView;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class RoomsView extends TableView {

    public RoomsView(String tableName, Table table) {
        super(tableName, table);
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        return div(
                super.getHtmlBody(handler),
                a(handler.route(GetRoomSearchHandler.class), "Search Rooms")
        );
    }

}
