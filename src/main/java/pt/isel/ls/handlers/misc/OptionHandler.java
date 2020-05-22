package pt.isel.ls.handlers.misc;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.misc.OptionView;

public final class OptionHandler extends RouteHandler {

    private final Iterable<Router.Route> routes;

    public OptionHandler(Iterable<Router.Route> routes) {
        super(
                Method.OPTION,
                "/",
                "Lists all available routes"
        );

        this.routes = routes;
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        return new HandlerResponse(new OptionView(routes));
    }

}
