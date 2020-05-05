package pt.isel.ls.handlers.booking;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.TableView;

public final class GetRoomBookingHandler extends RouteHandler {

    public GetRoomBookingHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/rooms/{rid}/bookings/{bid}",
                "Gets a specific booking from a room",
                provider
        );
    }

    /**
     * Gets a specific booking from a room
     * @param request The route request
     * @return returns a HandlerResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        int bid = request.getPathParameter("bid").toInt();

        Table table = new Table("User Id", "Begin time", "End time");
        Booking b = provider.execute(handler -> new BookingQueries(handler)
                .getBooking(bid));

        if (b.getRid() != rid) {
            throw new RouteException("No such booking found!");
        }

        table.addTableRow(b.getUid(), b.getBegin(), b.getEnd());
        return new HandlerResponse(new TableView("Booking Id: " + bid, table));
    }

}
