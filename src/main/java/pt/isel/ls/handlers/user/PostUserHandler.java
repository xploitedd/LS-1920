package pt.isel.ls.handlers.user;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.IdentifierView;

public final class PostUserHandler extends RouteHandler {

    public PostUserHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/users",
                "Creates a new user",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        String name = request.getParameter("name").get(0).toString();
        String email = request.getParameter("email").get(0).toString();

        User user = provider.execute(conn ->
                new UserQueries(conn).createNewUser(name, email));

        return new HandlerResponse(new IdentifierView("user", user.getUid()));
    }

}