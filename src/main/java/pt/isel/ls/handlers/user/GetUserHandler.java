package pt.isel.ls.handlers.user;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.TableView;

public final class GetUserHandler implements RouteHandler {

    private static final String DESCRIPTION = "Get a specific user";

    private final ConnectionProvider provider;

    public GetUserHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Get a specific user
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        Parameter paramUid = request.getPathParameter("uid");
        Table table = new Table("User Id", "Name", "Email");
        int uid = paramUid.toInt();
        User user = provider.execute(conn -> new UserQueries(conn)
                .getUser(uid));

        table.addTableRow(String.valueOf(user.getUid()), user.getName(), user.getEmail());
        return new HandlerResponse(new TableView(table));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

}
