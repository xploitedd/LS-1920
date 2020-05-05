package pt.isel.ls.handlers.label;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.TableView;

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
    public HandlerResponse execute(RouteRequest request) {
        Table table = new Table("Label Id", "Name");
        provider.execute(handler -> new LabelQueries(handler)
                .getLabels())
                .forEach(label ->
                        table.addTableRow(String.valueOf(label.getLid()), label.getName()));

        return new HandlerResponse(new TableView("Labels", table));
    }

}
