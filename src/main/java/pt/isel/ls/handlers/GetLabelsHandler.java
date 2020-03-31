package pt.isel.ls.handlers;

import pt.isel.ls.model.Label;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.TableView;

public final class GetLabelsHandler implements RouteHandler {

    private final ConnectionProvider provider;

    public GetLabelsHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets all existing Labels
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        Iterable<Label> iter = provider.execute(conn ->
                new LabelQueries(conn).getLabels());

        Table table = new Table("Label Id", "Name");
        for (Label label : iter) {
            table.addTableRow(String.valueOf(label.getLid()), label.getName());
        }

        return new RouteResponse(new TableView(table));
    }
}
