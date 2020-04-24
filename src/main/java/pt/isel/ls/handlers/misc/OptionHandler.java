package pt.isel.ls.handlers.misc;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.TableView;

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
        Table table = new Table("Method", "Template", "Description");
        for (Router.Route route : routes) {
            table.addTableRow(
                    route.getMethod(),
                    route.getRouteTemplate(),
                    route.getHandler().getDescription());
        }

        return new HandlerResponse(new TableView("Options Listing", table));
    }

}
