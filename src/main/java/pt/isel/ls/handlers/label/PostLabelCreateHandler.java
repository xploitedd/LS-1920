package pt.isel.ls.handlers.label;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;

public class PostLabelCreateHandler extends RouteHandler {

    public PostLabelCreateHandler(ConnectionProvider provider) {
        super(
                Method.POST,
                "/labels/create",
                "Creates a new Label",
                provider
        );
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        try {
            Label newLabel = new PostLabelHandler(provider).createLabel(request);
            return new HandlerResponse()
                    .redirect(GetLabelHandler.class, newLabel.getLid());
        } catch (AppException e) {
            return new HandlerResponse()
                    .redirect(GetLabelCreateHandler.class)
                    .getRedirect()
                    .setError(e.getMessage())
                    .getResponse();
        }
    }
}
