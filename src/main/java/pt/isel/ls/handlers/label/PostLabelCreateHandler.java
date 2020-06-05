package pt.isel.ls.handlers.label;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.model.Label;
import pt.isel.ls.router.StatusCode;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.ValidatorResult;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.error.HandlerError;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.label.LabelCreateView;

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
        PostLabelHandler plh = new PostLabelHandler(provider);
        ValidatorResult res = plh.getValidator().validate(request);
        if (res.hasErrors()) {
            return new HandlerResponse(new LabelCreateView(res.getErrors()))
                    .setStatusCode(StatusCode.BAD_REQUEST);
        }

        try {
            String name = res.getParameterValue("name");
            Label newLabel = new PostLabelHandler(provider).createLabel(name);
            return new HandlerResponse()
                    .redirect(GetLabelHandler.class, newLabel.getLid());
        } catch (AppException e) {
            HandlerError err = HandlerError.fromException(e);
            return new HandlerResponse(new LabelCreateView(err))
                    .setStatusCode(e.getStatusCode());
        }
    }
}
