package pt.isel.ls.handlers.misc;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;

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

    /**
     * Lists all available routes
     * @param request The request to be executed
     * @return a new HandlerResponse
     */
    @Override
    public HandlerResponse execute(Router router, RouteRequest request) {
        Table table = new Table("Method", "Template", "Description");
        for (Router.Route route : routes) {
            table.addTableRow(
                    route.getMethod(),
                    route.getRouteTemplate(),
                    route.getHandler().getDescription());
        }

        //TODO: return new HandlerResponse(new TableView("Options Listing", table));
        return null;
    }

}
