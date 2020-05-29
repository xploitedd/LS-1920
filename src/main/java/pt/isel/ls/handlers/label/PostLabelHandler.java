package pt.isel.ls.handlers.label;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.view.misc.IdentifierView;

public final class PostLabelHandler extends RouteHandler {

    public PostLabelHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/labels",
                "Creates a new label",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        Label label = createLabel(request);

        return new HandlerResponse(new IdentifierView("label", label.getLid()));
    }

    public Label createLabel(RouteRequest request) {
        String labelName = request.getParameter("name").get(0).toString();
        return provider.execute(handler ->
                new LabelQueries(handler).createNewLabel(labelName));
    }
}
