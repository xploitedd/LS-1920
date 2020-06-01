package pt.isel.ls.view.booking;

import pt.isel.ls.handlers.booking.GetRoomBookingsHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.handlers.user.GetUserHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.detail.HtmlDetailBuilder;
import pt.isel.ls.view.utils.detail.StringDetailBuilder;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class RoomBookingView extends View {

    private final Booking booking;

    public RoomBookingView(Booking booking) {
        super("Booking " + booking.getBid());
        this.booking = booking;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element details = new HtmlDetailBuilder()
                .withDetail("Id", booking.getBid())
                .withDetail("Room Id", booking.getRid())
                .withDetail("User", a(
                        handler.route(GetUserHandler.class, booking.getUid()),
                        "Show User"
                ))
                .withDetail("Begin", booking.getBegin())
                .withDetail("End", booking.getEnd())
                .build();

        return div(details);
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.write(new StringDetailBuilder()
                .withDetail("Id", booking.getBid())
                .withDetail("Room Id", booking.getRid())
                .withDetail("User Id", booking.getUid())
                .withDetail("Begin", booking.getBegin())
                .withDetail("End", booking.getEnd())
                .build());
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetRoomHandler.class, booking.getRid()),"Room");
        addNavEntry(handler.route(GetRoomBookingsHandler.class, booking.getRid()),"Room Bookings");
    }

}
