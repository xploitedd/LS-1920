package pt.isel.ls.handlers.misc;

import pt.isel.ls.app.http.HttpApplication;
import pt.isel.ls.app.http.HttpPool;
import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.request.parameter.Validator;
import pt.isel.ls.router.request.parameter.ValidatorResult;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.MessageView;

public class ListenHandler extends RouteHandler {

    private static final int DEFAULT_PORT = 8080;

    private final Router router;
    private final HttpPool httpPool;

    public ListenHandler(Router router, HttpPool httpPool) {
        super(
                Method.LISTEN,
                "/",
                "Enable the web server"
        );

        this.router = router;
        this.httpPool = httpPool;
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        Validator validator = new Validator()
                .addRule("port", p -> p.getUnique().toInt(), true);

        ValidatorResult res = validator.validate(request);
        if (res.hasErrors()) {
            throw new ValidatorException(res);
        }

        int port = (Integer) res.getOptionalParameter("port")
                .orElseGet(() -> {
                    String ps = System.getenv("PORT");
                    if (ps == null) {
                        return DEFAULT_PORT;
                    }

                    return Integer.valueOf(System.getenv("PORT"));
                });

        new HttpApplication(httpPool, router, port).run();
        return new HandlerResponse(new MessageView("New HTTP server created on port " + port));
    }

}
