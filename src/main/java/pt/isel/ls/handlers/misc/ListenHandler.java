package pt.isel.ls.handlers.misc;

import pt.isel.ls.app.http.HttpApplication;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;

public class ListenHandler extends RouteHandler {

    private final Router router;

    public ListenHandler(Router router) {
        super(
                Method.LISTEN,
                "/",
                "Enable the web server"
        );

        this.router = router;
    }

    @Override
    public HandlerResponse execute(RouteRequest request) {
        int port = request.getOptionalParameter("port")
                .map(s -> s.get(0).toInt())
                .orElse(8080);

        new HttpApplication(router, port).run();
        return null;
    }

}
