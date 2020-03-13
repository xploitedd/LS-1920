package pt.isel.ls.router;

public class RouteRequest {

    private Method method;
    private Path path;

    public RouteRequest(Method method, Path template) {
        this.method = method;
        this.path = template;
    }

    public Method getMethod() {
        return method;
    }

    public Path getPath() {
        return path;
    }

}
