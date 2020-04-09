package pt.isel.ls.router;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.Path;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.view.RouteExceptionView;

/**
 * Router is responsible for routing
 * all the requests to the destination
 * controller
 */
public class Router {

    private HashMap<Method, Set<Route>> methodRoutes = new HashMap<>();

    /**
     * Registers a new Route to this Router
     * @param template Template of the Route to be registered
     * @param handler Handler for the Route to be registered
     */
    public void registerRoute(Method method, RouteTemplate template, RouteHandler handler) {
        Route route = new Route(template, handler);
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
    public RouteHandler getHandler(RouteRequest request) {
        Set<Route> routes = methodRoutes.get(request.getMethod());
        for (Route r : routes) {
            RouteTemplate template = r.getRouteTemplate();
            Optional<HashMap<String, Parameter>> match = template.match(request.getPath());
            if (match.isPresent()) {
                request.setPathParameters(match.get());
                return r.getHandler();
            }
        }

        return r -> new RouteResponse(new RouteExceptionView(new RouteNotFoundException(r.getPath())))
                .setStatusCode(404);
    }

    public static class Route {

        private RouteTemplate routeTemplate;
        private RouteHandler handler;

        /**
         * Creates a new Route
         * @param routeTemplate Template of this route
         * @param handler Handler of this route
         */
        private Route(RouteTemplate routeTemplate, RouteHandler handler) {
            this.routeTemplate = routeTemplate;
            this.handler = handler;
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

    private static class RouteNotFoundException extends RouteException {

        private RouteNotFoundException(Path path) {
            super("The route " + path + " was not found!");
        }

    }

}
