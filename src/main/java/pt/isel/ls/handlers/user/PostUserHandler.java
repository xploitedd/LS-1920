package pt.isel.ls.handlers.user;

import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.handlers.validators.CreateUserValidator;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.misc.IdentifierView;

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
    public HandlerResponse execute(RouteRequest request) {
        CreateUserValidator validator = new CreateUserValidator(request, provider);
        if (validator.hasErrors()) {
            throw new ValidatorException(validator.getResult());
        }

        User user = createUser(validator.getName(), validator.getEmail());
        return new HandlerResponse(new IdentifierView("user", user.getUid()));
    }

    User createUser(String name, String email) {
        return provider.execute(handler ->
                new UserQueries(handler).createNewUser(name, email));
    }

}