package pt.isel.ls.handlers;

import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.TableView;

public final class GetRoomBookingsHandler implements RouteHandler {

    private final ConnectionProvider provider;

    public GetRoomBookingsHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets Bookings from Rooms
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        int rid = request.getPathParameter("rid").toInt();
        Iterable<Booking> iter = provider.execute(conn ->
                new BookingQueries(conn).getBookingsByRid(rid));

        Table table = new Table("Booking Id", "User Id", "Begin time", "End time");
        for (Booking booking : iter) {
            table.addTableRow(String.valueOf(booking.getBid()), String.valueOf(booking.getUid()),
                    booking.getBegin().toString(), booking.getEnd().toString());
        }

        return new HandlerResponse(new TableView(table));
    }
}
