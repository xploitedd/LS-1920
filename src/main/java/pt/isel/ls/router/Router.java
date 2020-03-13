package pt.isel.ls.router;

import pt.isel.ls.handlers.RouteHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Router is responsible for routing
 * all the requests to the destination
 * controller
 */
public class Router {

    private HashMap<Method, Optional<Map<RouteTemplate, RouteHandler>>> methodRoutes = new HashMap<>();

    /**
     * Registers a new Route to this Router
     * @param template Template of the Route to be registered
     * @param handler Handler for the Route to be registered
     */
    public void registerRoute(Method method, RouteTemplate template, RouteHandler handler) {
        Map<RouteTemplate, RouteHandler> templates = methodRoutes.get(method).orElse(new HashMap<>());
        templates.put(template, handler);
        // put the map, useful in case it does not exist
        methodRoutes.put(method, Optional.of(templates));
    }

    /**
     * Finds a route that matches the request
     * @param routeRequest Route requested
     * @return None if not found, Route if found
     */
    public Optional<Route> findRoute(RouteRequest routeRequest) {
        Method method = routeRequest.getMethod();
        Optional<Map<RouteTemplate, RouteHandler>> optionalTemplates = methodRoutes.get(method);
        if (optionalTemplates.isEmpty()) {
            return Optional.empty();
        }

        Map<RouteTemplate, RouteHandler> templates = optionalTemplates.get();
        for (RouteTemplate t : templates.keySet()) {
            if (t.matches(routeRequest.getPath())) {
                Route route = new Route(routeRequest, templates.get(t));
                return Optional.of(route);
            }
        }

        return Optional.empty();
    }

    public static class Route {

        private RouteRequest routeRequest;
        private RouteHandler handler;

        private Route(RouteRequest routeRequest, RouteHandler handler) {
            this.routeRequest = routeRequest;
            this.handler = handler;
        }

        public RouteRequest getRouteRequest() {
            return routeRequest;
        }

        public RouteHandler getHandler() {
            return handler;
        }

    }

}
