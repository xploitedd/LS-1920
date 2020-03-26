package pt.isel.ls.handlers;

import pt.isel.ls.model.User;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;

public final class PostUserHandler implements RouteHandler {

    private final DataSource dataSource;

    public PostUserHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        String name = request.getParameter("name").get(0).toString();
        String email = request.getParameter("email").get(0).toString();

        User user = new ConnectionProvider(dataSource)
                .execute(conn -> new UserQueries(conn).createNewUser(name, email));

        return new RouteResponse(new IdentifierView("user", user.getUid()));
    }
}