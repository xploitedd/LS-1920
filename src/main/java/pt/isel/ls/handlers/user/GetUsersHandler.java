package pt.isel.ls.handlers.user;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;

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
    public HandlerResponse execute(Router router, RouteRequest request) {
        Table table = new Table("User Id", "Name", "Email", "User Details");
        provider.execute(handler -> new UserQueries(handler)
                .getUsers())
                .forEach(user ->
                        table.addTableRow(
                                user.getUid(),
                                user.getName(),
                                user.getEmail(),
                                router.routeFromName(GetUserHandler.class, user.getUid())
                        ));

        //TODO: return new HandlerResponse(new TableView("Users", table));
        return null;
    }

}
