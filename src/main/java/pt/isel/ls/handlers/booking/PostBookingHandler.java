package pt.isel.ls.handlers.booking;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.utils.Time;
import pt.isel.ls.view.misc.IdentifierView;

import java.sql.Timestamp;

public final class PostBookingHandler extends RouteHandler {

    public PostBookingHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/rooms/{rid}/bookings",
                "Creates a new booking",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        int uid = request.getParameter("uid").get(0).toInt();
        long b = request.getParameter("begin").get(0).toTime();
        int duration = request.getParameter("duration").get(0).toInt();

        Booking booking = createBooking(rid, uid, b, duration);
        return new HandlerResponse(new IdentifierView("booking", booking.getBid()));
    }

    Booking createBooking(int rid, int uid, long begin, int duration) {
        Timestamp b = new Timestamp(begin);
        // duration in minutes
        Timestamp e = new Timestamp(begin + Time.minutesToMillis(duration));

        return provider.execute(handler ->
                new BookingQueries(handler).createNewBooking(rid, uid, b, e));
    }
}
