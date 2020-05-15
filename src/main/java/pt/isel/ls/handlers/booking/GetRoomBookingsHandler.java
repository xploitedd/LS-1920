package pt.isel.ls.handlers.booking;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.booking.RoomBookingsView;

public final class GetRoomBookingsHandler extends RouteHandler {

    public GetRoomBookingsHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/rooms/{rid}/bookings",
                "Gets all room bookings",
                provider
        );
    }

    /**
     * Gets all room bookings
     * @param request The route request
     * @return returns a HandlerResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(Router router, RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        Table table = new Table(
                "Booking Id",
                "User Id",
                "Begin time",
                "End time",
                "Booking Details"
        );

        provider.execute(handler -> new BookingQueries(handler)
                .getBookingsByRid(rid))
                .forEach(booking ->
                        table.addTableRow(
                                booking.getBid(),
                                booking.getUid(),
                                booking.getBegin(),
                                booking.getEnd(),
                                router.routeFromName(GetRoomBookingHandler.class, rid, booking.getBid())));

        return new HandlerResponse(new RoomBookingsView("Bookings", table, rid));
    }

}
