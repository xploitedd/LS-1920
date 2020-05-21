package pt.isel.ls.view.booking;

import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class RoomBookingsView extends View {

    private final int roomId;

    public RoomBookingsView(String tableName, Table table, int roomId) {
        super(tableName);
        this.roomId = roomId;
    }

    protected Node getHtmlBody(ViewHandler handler) {
        return div(
                super.getHtmlBody(handler),
                a(handler.route(GetRoomHandler.class, roomId),"Room")
        );
    }

}
