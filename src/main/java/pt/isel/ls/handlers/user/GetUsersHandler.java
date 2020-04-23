package pt.isel.ls.handlers.user;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.TableView;

public final class GetUsersHandler implements RouteHandler {

    private static final String DESCRIPTION = "Get all users";

    private final ConnectionProvider provider;

    public GetUsersHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Get all users
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        Table table = new Table("User Id", "Name", "Email");
        provider.execute(conn -> new UserQueries(conn)
                .getUsers())
                .forEach(user ->
                        table.addTableRow(String.valueOf(user.getUid()), user.getName(), user.getEmail()));

        return new HandlerResponse(new TableView("Users", table));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

}
