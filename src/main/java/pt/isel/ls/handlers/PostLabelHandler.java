package pt.isel.ls.handlers;

import pt.isel.ls.model.Label;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;

import javax.sql.DataSource;
import pt.isel.ls.view.console.IdentifierView;

public class PostLabelHandler implements RouteHandler {

    private DataSource dataSource;

    public PostLabelHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RouteResponse execute(RouteRequest request) throws Throwable {
        String labelName = request.getParameter("name").get(0);
        Label label = new ConnectionProvider(dataSource)
                .execute(conn -> new LabelQueries(conn).createNewLabel(labelName));

        return new RouteResponse(new IdentifierView("label", label.getLid()));
    }

}
