package pt.isel.ls.view.room;

import pt.isel.ls.handlers.booking.GetRoomBookingCreateHandler;
import pt.isel.ls.handlers.booking.GetRoomBookingsHandler;
import pt.isel.ls.handlers.label.GetLabelHandler;
import pt.isel.ls.handlers.room.GetRoomsHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.model.dsl.text.AnchorText;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.detail.HtmlDetailBuilder;
import pt.isel.ls.view.utils.detail.StringDetailBuilder;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.br;
import static pt.isel.ls.model.dsl.Dsl.div;

public class RoomView extends View {

    private final Room room;
    private final Iterable<Label> roomLabels;

    public RoomView(Room room, Iterable<Label> roomLabels) {
        super("Room " + room.getName());
        this.room = room;
        this.roomLabels = roomLabels;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        List<AnchorText> labels = StreamSupport.stream(roomLabels.spliterator(), false)
                .map(l -> a(handler.route(GetLabelHandler.class, l.getLid()), l.getName()))
                .collect(Collectors.toList());

        Element details = new HtmlDetailBuilder()
                .withDetail("Id", room.getRid())
                .withDetail("Name", room.getName())
                .withDetail("Capacity", room.getCapacity())
                .withDetail("Location", room.getLocation())
                .withDetail("Description", room.getDescription())
                .withDetail("Labels", labels)
                .build();

        return div(
                details,
                br(),
                a(handler.route(GetRoomBookingsHandler.class, room.getRid()), "Check Room Bookings"),
                br(),
                a(handler.route(GetRoomBookingCreateHandler.class), "Create Room Booking"),
                br(),
                a(handler.route(GetRoomsHandler.class), "Rooms")
        );
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        List<String> labels = StreamSupport.stream(roomLabels.spliterator(), false)
                .map(Label::toString)
                .collect(Collectors.toList());

        writer.write(new StringDetailBuilder()
                .withDetail("Id", room.getRid())
                .withDetail("Name", room.getName())
                .withDetail("Capacity", room.getCapacity())
                .withDetail("Location", room.getLocation())
                .withDetail("Description", room.getDescription())
                .withDetail("Labels", labels)
                .build());
    }

}
