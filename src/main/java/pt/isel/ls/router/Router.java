package pt.isel.ls.router;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import pt.isel.ls.handlers.RouteHandler;
import pt.isel.ls.view.console.ThrowableView;

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

    public RouteResponse executeRoute(Method method, Path path, RequestParameters<List<String>> parameters) {
        Set<Route> routes = methodRoutes.get(method);
        for (Route r : routes) {
            RouteTemplate template = r.getRouteTemplate();
            Optional<RequestParameters<String>> match = template.match(path);
            if (match.isPresent()) {
                try {
                    return r.getHandler()
                            .execute(new RouteRequest(path, match.get(), parameters));
                } catch (Throwable throwable) {
                    return new RouteResponse(new ThrowableView(throwable))
                            .setStatusCode(500);
                }
            }
        }

        return new RouteResponse(new ThrowableView(new RouteNotFoundException(path)))
                .setStatusCode(404);
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

    private static class RouteNotFoundException extends Exception {

        private RouteNotFoundException(Path path) {
            super("The route " + path + " was not found!");
        }

    }

}
