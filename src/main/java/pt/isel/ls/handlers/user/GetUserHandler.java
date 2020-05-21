package pt.isel.ls.handlers.user;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Booking;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.user.UserView;

import java.util.stream.Collectors;

public final class GetUserHandler extends RouteHandler {

    public GetUserHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/users/{uid}",
                "Get a specific user",
                provider
        );
    }

    /**
     * Get a specific user
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        int uid = request.getPathParameter("uid").toInt();
        User user = provider.execute(handler -> new UserQueries(handler).getUser(uid));
        Iterable<Booking> bookings = provider.execute(handler -> new BookingQueries(handler)
                    .getBookingsByUid(uid)
                    .collect(Collectors.toList()));

        return new HandlerResponse(new UserView(user, bookings));
    }

}
