package pt.isel.ls.handlers.label;

import pt.isel.ls.exceptions.router.RouteException;
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

    /**
     * Creates a new label
     * @param request The RouteRequest to be executed
     * @return a new HandlerResponse
     * @throws RouteException any exception that occurred
     */
    @Override
    public HandlerResponse execute(RouteRequest request) {
        String labelName = request.getParameter("name").get(0).toString();
        Label label = provider.execute(handler ->
                new LabelQueries(handler).createNewLabel(labelName));

        return new HandlerResponse(new IdentifierView("label", label.getLid()));
    }

}
