package pt.isel.ls.handlers.booking;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.misc.IdentifierView;

public final class DeleteBookingHandler extends RouteHandler {

    public DeleteBookingHandler(ConnectionProvider provider) {
        super(
                Method.DELETE,
                "/rooms/{rid}/bookings/{bid}",
                "Deletes a specific booking",
                provider
        );
    }

    /**
     * Deletes a Specific Booking
     * @param request The route request
     * @return returns a HandlerResponse with a IdentifierView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        int bid = request.getPathParameter("bid").toInt();

        int deleted = provider.execute(handler -> new BookingQueries(handler)
                .deleteBooking(rid, bid));

        if (deleted == 0) {
            throw new RouteException("No booking with rid=" + rid + " and bid=" + bid + " found");
        }

        return new HandlerResponse(new IdentifierView("deleted", "booking", bid));
    }

}
