package pt.isel.ls.router;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import pt.isel.ls.exceptions.router.RouteNotFoundException;
import pt.isel.ls.handlers.Handler;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.view.ExceptionView;

/**
 * Router is responsible for routing
 * all the requests to the destination
 * controller
 */
public class Router implements Iterable<Router.Route> {

    private final HashMap<Method, Set<Route>> methodRoutes = new HashMap<>();

    /**
     * Registers a new Route to this Router
     * @param handler Handler for the Route to be registered
     */
    public void registerRoute(RouteHandler handler) {
        Method method = handler.getMethod();
        Route route = new Route(handler.getTemplate(), handler, method);
        Set<Route> routes = methodRoutes.get(method);
        if (routes == null) {
            routes = new HashSet<>();
            routes.add(route);
            methodRoutes.put(method, routes);
        } else {
            routes.add(route);
        }
    }

    /**
     * Retrieve the handler for the specified request
     * @param request route request
     * @return handler associated with the specified request or
     * if not found the default 404 handler
     */
    public Handler getHandler(RouteRequest request) {
        Set<Route> routes = methodRoutes.get(request.getMethod());
        if (routes != null) {
            for (Route r : routes) {
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

    @Override
    public Iterator<Route> iterator() {
        return methodRoutes.values().stream()
                .flatMap(Collection::stream)
                .iterator();
    }

    public static class Route {

        private final RouteTemplate routeTemplate;
        private final RouteHandler handler;
        private final Method method;

        /**
         * Creates a new Route
         * @param routeTemplate Template of this route
         * @param handler Handler of this route
         * @param method the method of this route
         */
        private Route(RouteTemplate routeTemplate, RouteHandler handler, Method method) {
            this.routeTemplate = routeTemplate;
            this.handler = handler;
            this.method = method;
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
