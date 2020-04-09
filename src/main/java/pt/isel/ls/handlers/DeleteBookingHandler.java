package pt.isel.ls.handlers;

import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.IdentifierView;
import pt.isel.ls.view.MessageView;

public class DeleteBookingHandler implements RouteHandler {

    private final ConnectionProvider provider;

    public DeleteBookingHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Updates a Specific Booking with the new uid, begin and end
     * @param request The route request
     * @return returns a RouteResponse with a IdentifierView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        int rid = request.getPathParameter("rid").toInt();
        int bid = request.getPathParameter("bid").toInt();

        int deleted = provider.execute(conn ->
                new BookingQueries(conn).deleteBooking(rid, bid));
        if (deleted == 0) {
            return new RouteResponse(new MessageView("No booking with rid=" + rid + " and bid=" + bid + " found"));
        }
        return new RouteResponse(new IdentifierView("deleted", "booking", bid));
    }
}
