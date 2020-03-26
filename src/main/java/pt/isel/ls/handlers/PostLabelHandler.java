package pt.isel.ls.handlers;

import pt.isel.ls.model.Label;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;

import javax.sql.DataSource;
import pt.isel.ls.view.console.IdentifierView;

public final class PostLabelHandler implements RouteHandler {

    private final DataSource dataSource;

    public PostLabelHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws RouteException {
        String labelName = request.getParameter("name").get(0).toString();
        Label label = new ConnectionProvider(dataSource)
                .execute(conn -> new LabelQueries(conn).createNewLabel(labelName));

        return new RouteResponse(new IdentifierView("label", label.getLid()));
    }

}
