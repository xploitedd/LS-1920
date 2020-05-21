package pt.isel.ls.handlers.label;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;

public final class GetLabelsHandler extends RouteHandler {

    public GetLabelsHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/labels",
                "Gets all existing Labels",
                provider
        );
    }

    /**
     * Gets all existing Labels
     * @param request The route request
     * @return returns a HandlerResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public HandlerResponse execute(Router router, RouteRequest request) {
        Table table = new Table("Label Id", "Name", "Label Details");
        provider.execute(handler -> {
            new LabelQueries(handler)
                    .getLabels()
                    .forEach(label -> {
                        table.addTableRow(
                                label.getLid(),
                                label.getName(),
                                router.routeFromName(GetLabelHandler.class, label.getLid())
                        );
                    });

            return null;
        });

        //TODO: return new HandlerResponse(new TableView("Labels", table));
        return null;
    }

}
