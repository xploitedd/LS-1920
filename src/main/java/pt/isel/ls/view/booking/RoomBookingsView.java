package pt.isel.ls.view.booking;

import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.TableView;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class RoomBookingsView extends TableView {

    private final int roomId;

    public RoomBookingsView(String tableName, Table table, int roomId) {
        super(tableName, table);
        this.roomId = roomId;
    }

    //TODO: make the "Room" link return to this bookings' room
    protected Node getHtmlBody(ViewHandler handler) {
        return div(
                super.getHtmlBody(handler),
                a(handler.route(GetRoomHandler.class, roomId),"Room")
        );
    }

}
