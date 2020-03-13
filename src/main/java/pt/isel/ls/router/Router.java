package pt.isel.ls.router;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import pt.isel.ls.handlers.RouteHandler;

/**
 * Router is responsible for routing
 * all the requests to the destination
 * controller
 */
public class Router {

    private HashMap<Method, Optional<Set<Route>>> methodRoutes = new HashMap<>();

    /**
     * Registers a new Route to this Router
     * @param template Template of the Route to be registered
     * @param handler Handler for the Route to be registered
     */
    public void registerRoute(Method method, RouteTemplate template, RouteHandler handler) {
        Set<Route> routes = methodRoutes.get(method).orElse(new HashSet<>());
        routes.add(new Route(template, handler));
        // put the map, useful in case it does not exist
        methodRoutes.put(method, Optional.of(routes));
    }

    public void executeRoute(Method method, Path path, RequestParameters<List<String>> parameters) throws SQLException {
        Optional<Set<Route>> routes = methodRoutes.get(method);
        if (routes.isPresent()) {
            for (Route r : routes.get()) {
                RouteTemplate template = r.getRouteTemplate();
                Optional<RequestParameters<String>> match = template.match(path);
                if (match.isPresent()) {
                    r.getHandler().execute(new RouteRequest(path, match.get(), parameters));
                    return;
                }
            }
        }
    }

    public static class Route {

        private RouteTemplate routeTemplate;
        private RouteHandler handler;

        private Route(RouteTemplate routeTemplate, RouteHandler handler) {
            this.routeTemplate = routeTemplate;
            this.handler = handler;
        }

        public RouteTemplate getRouteTemplate() {
            return routeTemplate;
        }

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

}
