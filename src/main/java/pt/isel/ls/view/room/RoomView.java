package pt.isel.ls.view.room;

import pt.isel.ls.handlers.booking.GetRoomBookingsHandler;
import pt.isel.ls.handlers.label.GetLabelHandler;
import pt.isel.ls.handlers.room.GetRoomsHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.text.AnchorText;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.b;
import static pt.isel.ls.model.dsl.Dsl.br;
import static pt.isel.ls.model.dsl.Dsl.div;
import static pt.isel.ls.model.dsl.Dsl.text;

public class RoomView extends View {

    private final Room room;
    private final Iterable<Label> roomLabels;

    public RoomView(String title, Room room, Iterable<Label> roomLabels) {
        super(title);
        this.room = room;
        this.roomLabels = roomLabels;
    }

    protected Node getHtmlBody(ViewHandler handler) {
        List<AnchorText> labels = StreamSupport.stream(roomLabels.spliterator(), false)
                .map(l -> a(handler.route(GetLabelHandler.class, l.getLid()), l.getName()))
                .collect(Collectors.toList());

        return div(
                br(),
                div(
                        b("Id: "),
                        text(room.getRid())
                ),
                div(
                        b("Name: "),
                        text(room.getName())
                ),
                div(
                        b("Capacity: "),
                        text(room.getCapacity())
                ),
                div(
                        b("Location: "),
                        text(room.getLocation())
                ),
                div(
                        b("Description: "),
                        text(room.getDescription())
                ),
                div(
                        b("Labels: "),
                        text(labels)
                ),
                br(),
                a(handler.route(GetRoomBookingsHandler.class, room.getRid()), "Check Room Bookings"),
                br(),
                a(handler.route(GetRoomsHandler.class), "Rooms")
        );
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        super.renderText(handler, writer);
    }
}
