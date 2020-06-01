package pt.isel.ls.handlers.user;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.user.UserCreateView;

public class PostUserCreateHandler extends RouteHandler {
    public PostUserCreateHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/users/create",
                "Creates a new User",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        try {
            User newUser = new PostUserHandler(provider).createUser(request);
            return new HandlerResponse()
                    .redirect(GetUserHandler.class, newUser.getUid());
        } catch (AppException e) {
            return new HandlerResponse(new UserCreateView(e.getMessage()))
                    .setStatusCode(e.getStatusCode());
        }
    }
}
