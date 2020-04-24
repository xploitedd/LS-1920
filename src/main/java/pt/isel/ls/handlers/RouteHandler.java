package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteTemplate;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.sql.ConnectionProvider;

public abstract class RouteHandler implements Handler {

    protected final ConnectionProvider provider;
    private final Method method;
    private final RouteTemplate template;
    private final String description;

    public RouteHandler(Method method, String template, String description) {
        this(method, template, description, null);
    }

    public RouteHandler(Method method, String template, String description, ConnectionProvider provider) {
        this.provider = provider;
        this.method = method;
        this.template = RouteTemplate.of(template);
        this.description = description;
    }

    public final Method getMethod() {
        return method;
    }

    public final RouteTemplate getTemplate() {
        return template;
    }

    public final String getDescription() {
        return description;
    }

}
