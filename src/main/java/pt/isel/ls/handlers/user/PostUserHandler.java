package pt.isel.ls.handlers.user;

import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.User;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Validator;
import pt.isel.ls.router.request.validator.ValidatorResult;
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
        ValidatorResult res = getValidator().validate(request);
        if (res.hasErrors()) {
            throw new ValidatorException(res);
        }

        String name = res.getParameterValue("name");
        String email = res.getParameterValue("email");
        User user = createUser(name, email);

        return new HandlerResponse(new IdentifierView("user", user.getUid()));
    }

    User createUser(String name, String email) {
        return provider.execute(handler ->
                new UserQueries(handler).createNewUser(name, email));
    }

    Validator getValidator() {
        return new Validator()
                .addMapping("name", p -> p.getUnique().toString())
                .addMapping("email", p -> p.getUnique().toString())
                .addFilter("email", email -> provider.execute(handler -> {
                    new UserQueries(handler).checkEmailAvailability(email);
                    return null;
                }), String.class);
    }

}