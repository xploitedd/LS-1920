package pt.isel.ls.view;

import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.Router;

import java.io.PrintWriter;

public class ViewHandler {

    private final Router router;

    /**
     * Creates a new View Handler
     *
     * View Handlers are responsible for rendering views and
     * passing important information to them
     * @param router Application Router
     */
    public ViewHandler(Router router) {
        this.router = router;
    }

    /**
     * Render a specific view
     * @param view view to be rendered
     * @param type render type
     * @param writer where to render
     */
    public final void render(View view, ViewType type, PrintWriter writer) {
        view.render(this, type, writer);
    }

    /**
     * Get route path from handler class
     * @param clazz handler class
     * @param params path parameters
     * @return route path
     */
    public String route(Class<? extends RouteHandler> clazz, Object... params) {
        return router.route(clazz, params);
    }

}
