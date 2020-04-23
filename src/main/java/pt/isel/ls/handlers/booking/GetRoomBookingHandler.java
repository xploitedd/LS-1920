package pt.isel.ls.handlers.booking;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.MessageView;
import pt.isel.ls.view.TableView;

public final class GetRoomBookingHandler implements RouteHandler {

    private static final String DESCRIPTION = "Gets a specific booking from a room";

    private final ConnectionProvider provider;

    public GetRoomBookingHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets a specific booking from a room
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        int rid = request.getPathParameter("rid").toInt();
        int bid = request.getPathParameter("bid").toInt();

        Table table = new Table("User Id", "Begin time", "End time");
        Booking b = provider.execute(conn -> new BookingQueries(conn)
                .getBooking(bid));

        if (b.getRid() != rid) {
            return new HandlerResponse(new MessageView("No such booking found!"));
        }

        table.addTableRow(b.getUid(), b.getBegin(), b.getEnd());
        return new HandlerResponse(new TableView("Booking Id: " + bid, table));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
