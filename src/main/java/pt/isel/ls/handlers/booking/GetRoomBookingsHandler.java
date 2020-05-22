package pt.isel.ls.handlers.booking;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.booking.RoomBookingsView;

import java.util.stream.Collectors;

public final class GetRoomBookingsHandler extends RouteHandler {

    public GetRoomBookingsHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/rooms/{rid}/bookings",
                "Gets all room bookings",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        int rid = request.getPathParameter("rid").toInt();
        Iterable<Booking> bookings = provider.execute(handler -> new BookingQueries(handler)
                .getBookingsByRid(rid)
                .collect(Collectors.toList()));

        return new HandlerResponse(new RoomBookingsView(rid, bookings));
    }

}
