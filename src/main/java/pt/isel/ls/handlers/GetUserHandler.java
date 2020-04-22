package pt.isel.ls.handlers;

import pt.isel.ls.model.Table;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.TableView;

import java.util.Optional;

public final class GetUserHandler implements RouteHandler {

    private final ConnectionProvider provider;

    public GetUserHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets a specific user
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        Optional<Parameter> paramUid = request.getOptionalPathParameter("uid");
        Table table = new Table("User Id", "Name", "Email");
        if (paramUid.isPresent()) {
            int uid = paramUid.get().toInt();
            User user = provider.execute(conn -> new UserQueries(conn)
                    .getUser(uid));

            table.addTableRow(String.valueOf(user.getUid()), user.getName(), user.getEmail());
        } else {
            provider.execute(conn -> new UserQueries(conn)
                    .getUsers())
                    .forEach(user ->
                            table.addTableRow(String.valueOf(user.getUid()), user.getName(), user.getEmail()));
        }

        return new HandlerResponse(new TableView(table));
    }
}
