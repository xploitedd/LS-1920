package pt.isel.ls.view.user;

import pt.isel.ls.handlers.booking.GetRoomBookingHandler;
import pt.isel.ls.handlers.user.GetUsersHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.User;
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

public class UserView extends View {

    private final User user;
    private final Iterable<Booking> bookings;

    public UserView(User user, Iterable<Booking> bookings) {
        super("User " + user.getName());
        this.user = user;
        this.bookings = bookings;
    }

    @Override
    protected Node getHtmlBody(ViewHandler handler) {
        List<AnchorText> bookingLst = StreamSupport.stream(bookings.spliterator(), false)
                .map(b -> a(
                        handler.route(GetRoomBookingHandler.class, b.getRid(), b.getBid()),
                        String.valueOf(b.getBid())
                ))
                .collect(Collectors.toList());

        Element details = new HtmlDetailBuilder()
                .withDetail("Id", user.getUid())
                .withDetail("Name", user.getName())
                .withDetail("Email", user.getEmail())
                .withDetail("Bookings", bookingLst)
                .build();

        return div(
                details,
                br(),
                a(handler.route(GetUsersHandler.class), "Users")
        );
    }

    @Override
    protected void renderText(ViewHandler handler, PrintWriter writer) {
        List<String> bookingLst = StreamSupport.stream(bookings.spliterator(), false)
                .map(b -> String.valueOf(b.getBid()))
                .collect(Collectors.toList());

        writer.write(new StringDetailBuilder()
                .withDetail("Id", user.getUid())
                .withDetail("Name", user.getName())
                .withDetail("Email", user.getEmail())
                .withDetail("Bookings", bookingLst)
                .build());
    }

}
