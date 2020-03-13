package pt.isel.ls.router;

public class RouteTemplate {

    private RouteTemplate() {

    }

    public boolean matches(Path path) {
        return false;
    }

    public static RouteTemplate of(String routeString) {
        return null;
    }

}
