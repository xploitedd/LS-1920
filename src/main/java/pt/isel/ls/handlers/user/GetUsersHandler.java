package pt.isel.ls.handlers.user;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.user.UsersView;

import java.util.stream.Collectors;

public final class GetUsersHandler extends RouteHandler {

    public GetUsersHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/users",
                "Get all users",
                provider
        );
    }

    /**
     * Get all users
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        Iterable<User> users = provider.execute(handler -> new UserQueries(handler)
                .getUsers()
                .collect(Collectors.toList()));

        return new HandlerResponse(new UsersView(users));
    }

}
