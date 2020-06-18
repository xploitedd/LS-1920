package pt.isel.ls.handlers.user;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.handlers.validators.CreateUserValidator;
import pt.isel.ls.model.User;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.error.HandlerErrors;
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
        CreateUserValidator validator = new CreateUserValidator(request, provider);
        if (validator.hasErrors()) {
            return new HandlerResponse(new UserCreateView(validator.getResult().getErrors()))
                    .setStatusCode(StatusCode.BAD_REQUEST);
        }

        String name = validator.getName();
        String email = validator.getEmail();

        try {
            User newUser = new PostUserHandler(provider).createUser(name, email);
            return new HandlerResponse()
                    .redirect(GetUserHandler.class, newUser.getUid());
        } catch (AppException e) {
            HandlerErrors err = HandlerErrors.fromException(e);
            return new HandlerResponse(new UserCreateView(err))
                    .setStatusCode(e.getStatusCode());
        }
    }

}
