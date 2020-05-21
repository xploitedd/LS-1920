package pt.isel.ls.handlers.booking;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.view.booking.UserBookingsView;

import java.util.stream.Collectors;

public final class GetUserBookingsHandler extends RouteHandler {

    public GetUserBookingsHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/users/{uid}/bookings",
                "Gets bookings booked by a user",
                provider
        );
    }

    /**
     * Gets bookings booked by a user
     * @param request The route request
     * @return returns a HandlerResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        int uid = request.getPathParameter("uid").toInt();
        Iterable<Booking> bookings = provider.execute(handler -> new BookingQueries(handler)
                .getBookingsByUid(uid)
                .collect(Collectors.toList()));

        return new HandlerResponse(new UserBookingsView(uid, bookings));
    }

}
