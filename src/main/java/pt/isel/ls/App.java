package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.handlers.*;
import pt.isel.ls.router.Method;
import pt.isel.ls.router.RouteTemplate;
import pt.isel.ls.router.Router;

import javax.sql.DataSource;

public class App {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_DATABASE_URL";

    private static DataSource dataSource;
    private static Router router;

    public static void main(String[] args) {
        dataSource = getDataSource(System.getenv(DATABASE_CONNECTION_ENV));
        router = new Router();

        registerRoutes();
    }

    private static void registerRoutes() {
        // Register All Routes
        router.registerRoute(Method.EXIT, RouteTemplate.of("/"), new ExitHandler());
        // Room Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/rooms"), new PostRoomHandler());
        router.registerRoute(Method.GET, RouteTemplate.of("/rooms"), new GetRoomsHandler(dataSource));
        router.registerRoute(Method.GET, RouteTemplate.of("/rooms/{rid}"), new GetRoomHandler(dataSource));
        // Booking Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/rooms/{rid}/bookings"), new PostBookingHandler());
        router.registerRoute(Method.GET, RouteTemplate.of("/rooms/{rid}/bookings/{bid}"), new GetBookingsHandler(dataSource));
        // User Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/users"), new PostUserHandler());
        router.registerRoute(Method.GET, RouteTemplate.of("/users/{uid}"), new GetUserHandler(dataSource));
        router.registerRoute(Method.GET, RouteTemplate.of("/users/{uid}/bookings"), new GetUserBookingsHandler());
        // Label Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/labels"), new PostLabelHandler());
        router.registerRoute(Method.GET, RouteTemplate.of("/labels"), new GetLabelsHandler(dataSource));
        router.registerRoute(Method.GET, RouteTemplate.of("/labels/{lid}/rooms"), new GetLabeledRoomsHandler(dataSource));
    }

    static DataSource getDataSource(String connectionUrl) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(connectionUrl);
        return dataSource;
    }

}
