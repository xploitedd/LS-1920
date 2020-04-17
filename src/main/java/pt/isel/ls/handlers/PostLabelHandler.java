package pt.isel.ls.handlers;

import pt.isel.ls.model.Label;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;

import pt.isel.ls.view.IdentifierView;

public final class PostLabelHandler implements RouteHandler {

    private final ConnectionProvider provider;

    public PostLabelHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public HandlerResponse execute(RouteRequest request) throws RouteException {
        String labelName = request.getParameter("name").get(0).toString();
        Label label = provider.execute(conn ->
                new LabelQueries(conn).createNewLabel(labelName));

        return new HandlerResponse(new IdentifierView("label", label.getLid()));
    }

}
