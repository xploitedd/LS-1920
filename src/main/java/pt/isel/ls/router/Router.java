package pt.isel.ls.router;

import pt.isel.ls.handlers.RouteHandler;

import java.util.*;

public class Router {

    private HashMap<Method, Optional<Map<Route, RouteHandler>>> methodRoutes = new HashMap<>();

    public void registerRoute(Route route, RouteHandler handler) {
        Map<Route, RouteHandler> routes = methodRoutes.get(route.getMethod()).orElse(new HashMap<>());
        routes.put(route, handler);
        methodRoutes.put(route.getMethod(), Optional.of(routes));
    }

}
