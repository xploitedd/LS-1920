package pt.isel.ls.handlers;

import pt.isel.ls.router.RouteTemplate;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.sql.ConnectionProvider;

public abstract class RouteHandler implements Handler {

    protected final ConnectionProvider provider;
    private final Method method;
    private final RouteTemplate template;
    private final String description;

    /**
     * Creates a new RouteHandler with a method, template and description
     * @param method Method that is going to be handled
     * @param template The template that matches this handler
     * @param description The handler description
     */
    public RouteHandler(Method method, String template, String description) {
        this(method, template, description, null);
    }

    /**
     * Creates a new RouteHandler with a method, template and description
     *
     * Additionally passes a ConnectionProvider so connections to a Data Source
     * can be made by this handler
     * @param method Method that is going to be handled
     * @param template The template that matches this handler
     * @param description The handler description
     * @param provider The Connection Provider
     */
    public RouteHandler(Method method, String template, String description, ConnectionProvider provider) {
        this.provider = provider;
        this.method = method;
        this.template = RouteTemplate.of(template);
        this.description = description;
    }

    /**
     * Get the method of this handler
     * @return handler method
     */
    public final Method getMethod() {
        return method;
    }

    /**
     * Get the template for this handler
     * @return handler template
     */
    public final RouteTemplate getTemplate() {
        return template;
    }

    /**
     * Get the description of this handler
     * @return handler description
     */
    public final String getDescription() {
        return description;
    }

}
