package pt.isel.ls.handlers.user;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.user.UserCreateView;

public class GetUserCreateHandler extends RouteHandler {

    public GetUserCreateHandler() {
        super(
                Method.GET,
                "/users/create",
                "Return a html page with a form to create a user");
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        return new HandlerResponse(new UserCreateView());
    }

}
