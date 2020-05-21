package pt.isel.ls.view.booking;

import pt.isel.ls.handlers.booking.GetRoomBookingHandler;
import pt.isel.ls.handlers.user.GetUserHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.dsl.Node;
import pt.isel.ls.model.dsl.elements.Element;
import pt.isel.ls.view.View;
import pt.isel.ls.view.ViewHandler;
import pt.isel.ls.view.utils.table.HtmlTableBuilder;
import pt.isel.ls.view.utils.table.StringTableBuilder;

import java.io.PrintWriter;

import static pt.isel.ls.model.dsl.Dsl.a;
import static pt.isel.ls.model.dsl.Dsl.br;
import static pt.isel.ls.model.dsl.Dsl.div;

public class UserBookingsView extends View {

    private final int userId;
    private final Iterable<Booking> bookings;

    public UserBookingsView(int userId, Iterable<Booking> bookings) {
        super("Bookings for User " + userId);
        this.userId = userId;
        this.bookings = bookings;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        Element el = new HtmlTableBuilder<>(bookings)
                .withColumn("Id", Booking::getBid)
                .withColumn("Room Id", Booking::getRid)
                .withColumn("Begin", Booking::getBegin)
                .withColumn("End", Booking::getEnd)
                .withColumn("Booking Link", b -> a(
                        handler.route(GetRoomBookingHandler.class, b.getRid(), b.getBid()),
                        "Show details"
                ))
                .build();

        return div(
                el,
                br(),
                a(handler.route(GetUserHandler.class, userId),"User")
        );
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        writer.write(new StringTableBuilder<>(bookings)
                .withColumn("Id", Booking::getBid)
                .withColumn("Room Id", Booking::getRid)
                .withColumn("Begin", Booking::getBegin)
                .withColumn("End", Booking::getEnd)
                .build());
    }

}
