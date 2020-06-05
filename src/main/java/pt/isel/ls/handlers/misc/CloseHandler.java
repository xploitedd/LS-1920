package pt.isel.ls.handlers.misc;

import pt.isel.ls.app.http.HttpPool;
import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.validator.Validator;
import pt.isel.ls.router.request.validator.ValidatorResult;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.MessageView;

public class CloseHandler extends RouteHandler {

    private final HttpPool httpPool;

    public CloseHandler(HttpPool httpPool) {
        super(
                Method.CLOSE,
                "/",
                "Closes a running http servlet"
        );

        this.httpPool = httpPool;
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        Validator validator = new Validator()
                .addMapping("port", p -> p.getUnique().toInt());

        ValidatorResult res = validator.validate(request);
        if (res.hasErrors()) {
            throw new ValidatorException(res);
        }

        int port = res.getParameterValue("port");
        httpPool.terminate(port);
        return new HandlerResponse(new MessageView("HTTP Server on port " + port + " terminated!"));
    }

}
