package pt.isel.ls.view.booking;

import pt.isel.ls.handlers.booking.GetRoomBookingHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.table.HtmlTableBuilder;
import pt.isel.ls.view.utils.table.StringTableBuilder;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.div;

public class RoomBookingsView extends View {

    private final int roomId;
    private final Iterable<Booking> bookings;

    public RoomBookingsView(int roomId, Iterable<Booking> bookings) {
        super("Bookings for Room " + roomId);
        this.roomId = roomId;
        this.bookings = bookings;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element el = new HtmlTableBuilder<>(bookings)
                .withColumn("Id", Booking::getBid)
                .withColumn("User Id", Booking::getUid)
                .withColumn("Begin", Booking::getBegin)
                .withColumn("End", Booking::getEnd)
                .withColumn("Booking Link", b -> a(
                        handler.route(GetRoomBookingHandler.class, b.getRid(), b.getBid()),
                        "Show details"
                ))
                .build();

        return div(el);
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.write(new StringTableBuilder<>(bookings)
                .withColumn("Id", Booking::getBid)
                .withColumn("User Id", Booking::getUid)
                .withColumn("Begin", Booking::getBegin)
                .withColumn("End", Booking::getEnd)
                .build());
    }

    @Override
    protected void registerNavLinks(ViewHandler handler) {
        addNavEntry(handler.route(GetRoomHandler.class, roomId),"Room");
    }

}
