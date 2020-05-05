package pt.isel.ls.handlers.booking;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.utils.Time;
import pt.isel.ls.view.IdentifierView;

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

    /**
     * Creates a new booking
     * @param request The route request
     * @return returns a new HandlerResponse
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        int uid = request.getParameter("uid").get(0).toInt();
        long b = request.getParameter("begin").get(0).toLong();
        int duration = request.getParameter("duration").get(0).toInt();

        Timestamp begin = new Timestamp(b);
        // duration in minutes
        Timestamp end = new Timestamp(b + Time.minutesToMillis(duration));

        Booking booking = provider.execute(handler ->
                new BookingQueries(handler).createNewBooking(rid, uid, begin, end));

        return new HandlerResponse(new IdentifierView("booking", booking.getBid()));
    }

}
