package pt.isel.ls.handlers;

import pt.isel.ls.model.Label;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;

import javax.sql.DataSource;

import pt.isel.ls.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.console.TableView;

public final class GetLabelsHandler implements RouteHandler {

    private final DataSource dataSource;

    public GetLabelsHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets all existing Labels
     * @param request The route request
     * @return returns a RouteResponse with a tableView for the router
     * @throws RouteException Sent to the router
     */
    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        Iterable<Label> iter = new ConnectionProvider(dataSource)
                .execute(conn -> new LabelQueries(conn).getLabels());

        Table table = new Table("Label Id", "Name");
        for (Label label : iter) {
            table.addTableRow(String.valueOf(label.getLid()), label.getName());
        }

        return new RouteResponse(new TableView(table));
    }
}
