package pt.isel.ls.handlers.booking;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.utils.Time;
import pt.isel.ls.view.IdentifierView;

import java.sql.Timestamp;

public final class PutBookingHandler extends RouteHandler {

    public PutBookingHandler(ConnectionProvider provider) {
        super(
                Method.PUT,
                "/rooms/{rid}/bookings/{bid}",
                "Updates a booking",
                provider
        );
    }

    /**
     * Updates a Specific Booking with the new uid, begin and end
     * @param request The route request
     * @return returns a HandlerResponse with a IdentifierView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        int bid = request.getPathParameter("bid").toInt();
        int newUid = request.getParameter("uid").get(0).toInt();
        long newBegin = request.getParameter("begin").get(0).toLong();
        int duration = request.getParameter("duration").get(0).toInt();

        Timestamp newB = new Timestamp(newBegin);
        Timestamp newE = new Timestamp(newBegin + Time.minutesToMillis(duration));

        Booking booking = provider.execute(handler -> new BookingQueries(handler)
                .editBooking(rid, bid, newUid, newB, newE));

        return new HandlerResponse(new IdentifierView("updated", "booking", booking.getBid()));
    }

}
