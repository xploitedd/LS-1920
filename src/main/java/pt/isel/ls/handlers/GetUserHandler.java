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

import java.util.ArrayList;
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
        Iterable<User> iter = provider.execute(conn -> {
            Optional<Parameter> paramUid = request.getOptionalPathParameter("uid");
            if (paramUid.isPresent()) {
                ArrayList<User> userList = new ArrayList<>(1);
                userList.add(new UserQueries(conn).getUser(paramUid.get().toInt()));
                return userList;
            } else {
                return new UserQueries(conn).getUsers();
            }
        });

        Table table = new Table("User Id", "Name", "Email");
        for (User user : iter) {
            table.addTableRow(String.valueOf(user.getUid()), user.getName(), user.getEmail());
        }

        return new HandlerResponse(new TableView(table));
    }
}
