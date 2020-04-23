package pt.isel.ls.handlers.booking;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.TableView;

public final class GetRoomBookingsHandler implements RouteHandler {

    private static final String DESCRIPTION = "Gets all room bookings";

    private final ConnectionProvider provider;

    public GetRoomBookingsHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets all room bookings
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        int rid = request.getPathParameter("rid").toInt();
        Table table = new Table("Booking Id", "User Id", "Begin time", "End time");
        provider.execute(conn -> new BookingQueries(conn)
                .getBookingsByRid(rid))
                .forEach(booking ->
                        table.addTableRow(
                                String.valueOf(booking.getBid()),
                                String.valueOf(booking.getUid()),
                                booking.getBegin().toString(),
                                booking.getEnd().toString()));

        return new HandlerResponse(new TableView("Bookings", table));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
