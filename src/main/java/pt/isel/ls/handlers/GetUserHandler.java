package pt.isel.ls.handlers;

import pt.isel.ls.model.Table;
import pt.isel.ls.model.User;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.console.TableView;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Optional;

public class GetUserHandler implements RouteHandler {

    private DataSource dataSource;

    public GetUserHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets a specific user
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws Throwable Sent to the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {

        Iterable<User> iter = new ConnectionProvider(dataSource)
                .execute(conn -> {
                    Optional<String> paramUid = request.getOptionalPathParameter("uid");
                    if (paramUid.isPresent()) {
                        ArrayList<User> userList = new ArrayList<>(1);
                        userList.add(new UserQueries(conn).getUser(Integer.parseInt(paramUid.get())));
                        return userList;
                    } else {
                        return new UserQueries(conn).getUsers();
                    }
                });

        Table table = new Table("User Id", "Name", "Email");
        for (User user : iter) {
            table.addTableRow(String.valueOf(user.getUid()), user.getName(), user.getEmail());
        }

        return new RouteResponse(new TableView(table));
    }
}
