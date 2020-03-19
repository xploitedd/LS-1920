package pt.isel.ls.handlers;

import java.util.Iterator;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Model;
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
        Iterable<Model> iter = new ConnectionProvider(dataSource).execute(conn -> {
            String labelName = request.getParameter("name").get(0);
            return new LabelQueries(conn).createNewLabel(labelName);
        });

        Iterator<Model> it = iter.iterator();
        if (!it.hasNext()) {
            // TODO: throw error
        }

        Label label = (Label) it.next();
        return new RouteResponse(new IdentifierView("label", label.getLid()));
    }

}
