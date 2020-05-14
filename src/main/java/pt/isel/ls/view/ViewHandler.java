package pt.isel.ls.view;

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

    public String route(String name) {
        return router.routeFromName(name);
    }

}
