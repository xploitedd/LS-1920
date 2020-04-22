package pt.isel.ls.handlers;

import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.IdentifierView;

import java.sql.Timestamp;

public class PutBookingHandler implements RouteHandler {

    private static final String DESCRIPTION = "Updates a booking";

    private final ConnectionProvider provider;

    public PutBookingHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Updates a Specific Booking with the new uid, begin and end
     * @param request The route request
     * @return returns a RouteResponse with a IdentifierView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        int rid = request.getPathParameter("rid").toInt();
        int bid = request.getPathParameter("bid").toInt();
        int newUid = request.getParameter("uid").get(0).toInt();
        long newBegin = request.getParameter("begin").get(0).toLong();
        long duration = request.getParameter("duration").get(0).toLong();
        Timestamp newB = new Timestamp(newBegin);
        Timestamp newE = new Timestamp(newBegin + duration);

        Booking booking = provider.execute(conn ->
                new BookingQueries(conn).editBooking(rid, bid, newUid, newB, newE));

        return new HandlerResponse(new IdentifierView("updated", "booking", booking.getBid()));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

}
