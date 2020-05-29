package pt.isel.ls.handlers.label;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.label.LabelCreateView;

public class GetLabelCreateHandler extends RouteHandler {

    public GetLabelCreateHandler(ConnectionProvider provider) {
        super(
                Method.GET,
                "/labels/create",
                "Return a html page with a form to create a label",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        return new HandlerResponse(new LabelCreateView());
    }
}
