package pt.isel.ls.handlers.user;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.handlers.booking.GetRoomBookingHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.model.User;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
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
    public HandlerResponse execute(Router router, RouteRequest request) {
        Parameter paramUid = request.getPathParameter("uid");
        Table table = new Table("User Id", "Name", "Email", "Bookings");
        int uid = paramUid.toInt();
        provider.execute(handler -> {
            User user = new UserQueries(handler)
                    .getUser(uid);

            Iterable<String> bookings = new BookingQueries(handler)
                    .getBookingsByUid(uid)
                    .map(b -> router.routeFromName(GetRoomBookingHandler.class, b.getRid(), b.getBid()))
                    .collect(Collectors.toList());

            table.addTableRow(user.getUid(), user.getName(), user.getEmail(), bookings);
            return null;
        });

        return new HandlerResponse(new UserView("User: " + uid, table));
    }

}
