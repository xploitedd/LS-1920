package pt.isel.ls.handlers;

import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.TableView;

public final class GetUserBookingsHandler implements RouteHandler {

    private final ConnectionProvider provider;

    public GetUserBookingsHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets bookings booked by a user
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        int uid = request.getPathParameter("uid").toInt();
        Iterable<Booking> iter = provider.execute(conn ->
                new BookingQueries(conn).getBookingsByUid(uid));

        Table table = new Table("Booking Id", "Room Id", "Begin time", "End time");
        for (Booking booking : iter) {
            table.addTableRow(String.valueOf(booking.getBid()), String.valueOf(booking.getRid()),
                    booking.getBegin().toString(), booking.getEnd().toString());
        }

        return new HandlerResponse(new TableView(table));

    }
}
