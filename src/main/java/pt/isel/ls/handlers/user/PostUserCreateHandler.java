package pt.isel.ls.handlers.user;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.User;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.ValidatorResult;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.error.HandlerError;
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
        PostUserHandler puh = new PostUserHandler(provider);
        ValidatorResult res = puh.getValidator().validate(request);
        if (res.hasErrors()) {
            return new HandlerResponse(new UserCreateView(res.getErrors(), request))
                    .setStatusCode(StatusCode.BAD_REQUEST);
        }

        String name = res.getParameterValue("name");
        String email = res.getParameterValue("email");

        try {
            User newUser = new PostUserHandler(provider).createUser(name, email);
            return new HandlerResponse()
                    .redirect(GetUserHandler.class, newUser.getUid());
        } catch (AppException e) {
            HandlerError err = HandlerError.fromException(e);
            return new HandlerResponse(new UserCreateView(err))
                    .setStatusCode(e.getStatusCode());
        }
    }
}
