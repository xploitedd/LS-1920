package pt.isel.ls.view.booking;

import pt.isel.ls.handlers.booking.GetRoomBookingsHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.view.TableView;
import pt.isel.ls.view.ViewHandler;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class RoomBookingView extends TableView {

    private final int roomId;

    public RoomBookingView(String tableName, Table table, int roomId) {
        super(tableName, table);
        this.roomId = roomId;
    }

    protected Node getHtmlBody(ViewHandler handler) {
        return div(
                super.getHtmlBody(handler),
                a(handler.route(GetRoomBookingsHandler.class, roomId),"Room Bookings")
        );
    }

}
