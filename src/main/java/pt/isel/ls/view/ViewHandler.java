package pt.isel.ls.view;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.Router;

import java.io.PrintWriter;

public class ViewHandler {

    private final Router router;

    public ViewHandler(Router router) {
        this.router = router;
    }

    public final void render(View view, ViewType type, PrintWriter writer) {
        view.render(this, type, writer);
    }

    public String route(Class<? extends RouteHandler> clazz, Object... params) {
        return router.routeFromName(clazz, params);
    }

}
