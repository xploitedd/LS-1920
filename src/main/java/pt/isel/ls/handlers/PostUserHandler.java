package pt.isel.ls.handlers;

import pt.isel.ls.model.User;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.IdentifierView;

public final class PostUserHandler implements RouteHandler {

    private final ConnectionProvider provider;

    public PostUserHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        String name = request.getParameter("name").get(0).toString();
        String email = request.getParameter("email").get(0).toString();

        User user = provider.execute(conn ->
                new UserQueries(conn).createNewUser(name, email));

        return new RouteResponse(new IdentifierView("user", user.getUid()));
    }
}