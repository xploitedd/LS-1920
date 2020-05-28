package pt.isel.ls.router;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.exceptions.router.RouteNotFoundException;
import pt.isel.ls.handlers.Handler;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.misc.ExceptionView;

/**
 * Router is responsible for routing
 * all the requests to the destination
 * controller
 */
public class Router implements Iterable<Router.Route> {

    private final HashMap<Method, Map<Class<? extends RouteHandler>, Route>> methodRoutes = new HashMap<>();

    /**
     * Registers a new Route to this Router
     * @param handler Handler for the Route to be registered
     */
    public void registerRoute(RouteHandler handler) {
        Class<? extends RouteHandler> clazz = handler.getClass();
        if (clazz.isAnonymousClass()) {
            throw new RouteException("Handler class cannot be anonymous!");
        }

        Method method = handler.getMethod();
        Route route = new Route(clazz, handler.getTemplate(), handler, method);
        Map<Class<? extends RouteHandler>, Route> routes = methodRoutes.get(method);
        if (routes == null) {
            routes = new HashMap<>();
            routes.put(clazz, route);
            methodRoutes.put(method, routes);
        } else {
            routes.put(clazz, route);
        }
    }

    /**
     * Retrieve the handler for the specified request
     * @param request route request
     * @return handler associated with the specified request or
     * if not found the default 404 handler
     */
    public Handler getHandler(RouteRequest request) {
        Map<Class<? extends RouteHandler>, Route> routes = methodRoutes.get(request.getMethod());
        if (routes != null) {
            for (Route r : routes.values()) {
                RouteTemplate template = r.getRouteTemplate();
                Optional<HashMap<String, Parameter>> match = template.match(request.getPath());
                if (match.isPresent()) {
                    request.setPathParameters(match.get());
                    return r.getHandler();
                }
            }
        }

        return r -> new HandlerResponse(new ExceptionView(new RouteNotFoundException(r.getPath())))
                .setStatusCode(404);
    }

    /**
     * Get the route path that is handled by the specified RouteHandler
     * @param clazz RouteHandler class to get the path from
     * @param params path parameters
     * @return a path if the router has the specified class, otherwise null
     */
    public String route(Class<? extends RouteHandler> clazz, Object... params) {
        Route route = getRoute(clazz);
        if (route != null) {
            return route.getRouteTemplate()
                    .constructPathFromTemplate(params)
                    .toString();
        }

        return null;
    }

    /**
     * Get the route that is handled by the specified RouteHandler
     * @param clazz RouteHandler class to get the path from
     * @return a route if the router has the specified class, otherwise null
     */
    public Route getRoute(Class<? extends RouteHandler> clazz) {
        for (Map<Class<? extends RouteHandler>, Route> routes : methodRoutes.values()) {
            Route route = routes.get(clazz);
            if (route != null) {
                return route;
            }
        }

        return null;
    }

    @Override
    public Iterator<Route> iterator() {
        return methodRoutes.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .iterator();
    }

    public static class Route {

        private final Class<? extends RouteHandler> handlerClass;
        private final RouteTemplate routeTemplate;
        private final RouteHandler handler;
        private final Method method;

        /**
         * Creates a new Route
         * @param routeTemplate Template of this route
         * @param handler Handler of this route
         * @param method the method of this route
         */
        private Route(
                Class<? extends RouteHandler> handlerClass,
                RouteTemplate routeTemplate,
                RouteHandler handler,
                Method method) {

            this.handlerClass = handlerClass;
            this.routeTemplate = routeTemplate;
            this.handler = handler;
            this.method = method;
        }

        public Class<? extends RouteHandler> getHandlerClass() {
            return handlerClass;
        }

        /**
         * Get Route Template
         * @return Route Template
         */
        public RouteTemplate getRouteTemplate() {
            return routeTemplate;
        }

        /**
         * Get Route Handler
         * @return Route Handler
         */
        public RouteHandler getHandler() {
            return handler;
        }

        /**
         * Get Route Method
         * @return Route Method
         */
        public Method getMethod() {
            return method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Route route = (Route) o;
            return routeTemplate.equals(route.routeTemplate)
                    && handler.equals(route.handler);
        }

        @Override
        public int hashCode() {
            return Objects.hash(routeTemplate, handler);
        }

    }

}
