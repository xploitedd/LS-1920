package pt.isel.ls.handlers.misc;

import pt.isel.ls.app.http.HttpApplication;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.MessageView;

public class ListenHandler extends RouteHandler {

    private static final int DEFAULT_PORT = 8080;

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
                .orElseGet(() -> {
                    String ps = System.getenv("PORT");
                    if (ps == null) {
                        return DEFAULT_PORT;
                    }

                    return Integer.valueOf(System.getenv("PORT"));
                });

        new HttpApplication(router, port).run();
        return new HandlerResponse(new MessageView("New HTTP server created on port " + port));
    }

}
