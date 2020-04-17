package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

import pt.isel.ls.handlers.ExitHandler;
import pt.isel.ls.handlers.GetLabeledRoomsHandler;
import pt.isel.ls.handlers.GetLabelsHandler;
import pt.isel.ls.handlers.GetRoomBookingsHandler;
import pt.isel.ls.handlers.GetRoomsHandler;
import pt.isel.ls.handlers.GetTimeHandler;
import pt.isel.ls.handlers.GetUserBookingsHandler;
import pt.isel.ls.handlers.GetUserHandler;
import pt.isel.ls.handlers.PostBookingHandler;
import pt.isel.ls.handlers.PostLabelHandler;
import pt.isel.ls.handlers.PostRoomHandler;
import pt.isel.ls.handlers.PostUserHandler;
import pt.isel.ls.handlers.PutBookingHandler;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.RouteTemplate;
import pt.isel.ls.router.Router;
import pt.isel.ls.sql.ConnectionProvider;

public class App implements Runnable {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_DATABASE_URL";

    private final AppProcessor processor;
    private final ConnectionProvider connProvider;
    private final Router router;

    private App() {
        String url = System.getenv(DATABASE_CONNECTION_ENV);
        if (url == null) {
            System.err.println("Please set the " + DATABASE_CONNECTION_ENV + " environment variable");
            System.exit(1);
        }

        this.connProvider = new ConnectionProvider(getDataSource(url));
        this.router = new Router();
        this.processor = new AppProcessor(router);

        registerRoutes();
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    @Override
    public void run() {
        ConsoleApplication consoleApp = new ConsoleApplication(processor);
        consoleApp.run();
    }

    /**
     * Registers all the routes for this app
     */
    private void registerRoutes() {
        // Register All Routes
        router.registerRoute(Method.EXIT, RouteTemplate.of("/"),
                new ExitHandler());
        router.registerRoute(Method.GET, RouteTemplate.of("/time"),
                new GetTimeHandler());

        // Room Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/rooms"),
                new PostRoomHandler(connProvider));
        router.registerRoute(Method.GET, RouteTemplate.of("/rooms/{rid}?"),
                new GetRoomsHandler(connProvider));

        // Booking Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/rooms/{rid}/bookings"),
                new PostBookingHandler(connProvider));
        router.registerRoute(Method.GET, RouteTemplate.of("/rooms/{rid}/bookings/{bid}?"),
                new GetRoomBookingsHandler(connProvider));
        router.registerRoute(Method.PUT, RouteTemplate.of("/rooms/{rid}/bookings/{bid}"),
                new PutBookingHandler(connProvider));
        router.registerRoute(Method.DELETE, RouteTemplate.of("/rooms/{rid}/bookings/{bid}"),
                new PutBookingHandler(connProvider));

        // User Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/users"),
                new PostUserHandler(connProvider));
        router.registerRoute(Method.GET, RouteTemplate.of("/users/{uid}?"),
                new GetUserHandler(connProvider));
        router.registerRoute(Method.GET, RouteTemplate.of("/users/{uid}/bookings"),
                new GetUserBookingsHandler(connProvider));

        // Label Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/labels"),
                new PostLabelHandler(connProvider));
        router.registerRoute(Method.GET, RouteTemplate.of("/labels"),
                new GetLabelsHandler(connProvider));
        router.registerRoute(Method.GET, RouteTemplate.of("/labels/{lid}/rooms"),
                new GetLabeledRoomsHandler(connProvider));
    }

    /**
     * Gets a new data source from the connection url
     * @param connectionUrl connection url to retrieve the data source from
     * @return a new data source
     */
    private DataSource getDataSource(String connectionUrl) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(connectionUrl);
        return dataSource;
    }

}
