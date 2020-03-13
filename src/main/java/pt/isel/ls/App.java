package pt.isel.ls;

import java.util.Optional;
import java.util.Scanner;
import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.handlers.ExitHandler;
import pt.isel.ls.handlers.GetLabeledRoomsHandler;
import pt.isel.ls.handlers.GetLabelsHandler;
import pt.isel.ls.handlers.GetRoomBookingsHandler;
import pt.isel.ls.handlers.GetRoomsHandler;
import pt.isel.ls.handlers.GetUserBookingsHandler;
import pt.isel.ls.handlers.GetUserHandler;
import pt.isel.ls.handlers.PostBookingHandler;
import pt.isel.ls.handlers.PostLabelHandler;
import pt.isel.ls.handlers.PostRoomHandler;
import pt.isel.ls.handlers.PostUserHandler;

import javax.sql.DataSource;
import pt.isel.ls.router.Method;
import pt.isel.ls.router.Path;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.router.RouteTemplate;
import pt.isel.ls.router.Router;

public class App {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_DATABASE_URL";

    private static DataSource dataSource;
    private static Router router;

    public static void main(String[] args) {
        dataSource = getDataSource(System.getenv(DATABASE_CONNECTION_ENV));
        router = new Router();

        registerRoutes();
        startApp();
    }

    private static void registerRoutes() {
        // Register All Routes
        router.registerRoute(Method.EXIT, RouteTemplate.of("/"),
                new ExitHandler());

        // Room Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/rooms"),
                new PostRoomHandler(dataSource));
        router.registerRoute(Method.GET, RouteTemplate.of("/rooms/{rid}?"),
                new GetRoomsHandler(dataSource));

        // Booking Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/rooms/{rid}/bookings"),
                new PostBookingHandler(dataSource));
        router.registerRoute(Method.GET, RouteTemplate.of("/rooms/{rid}/bookings/{bid}?"),
                new GetRoomBookingsHandler(dataSource));

        // User Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/users"),
                new PostUserHandler(dataSource));
        router.registerRoute(Method.GET, RouteTemplate.of("/users/{uid}"),
                new GetUserHandler(dataSource));
        router.registerRoute(Method.GET, RouteTemplate.of("/users/{uid}/bookings"),
                new GetUserBookingsHandler(dataSource));

        // Label Handlers
        router.registerRoute(Method.POST, RouteTemplate.of("/labels"),
                new PostLabelHandler(dataSource));
        router.registerRoute(Method.GET, RouteTemplate.of("/labels"),
                new GetLabelsHandler(dataSource));
        router.registerRoute(Method.GET, RouteTemplate.of("/labels/{lid}/rooms"),
                new GetLabeledRoomsHandler(dataSource));
    }

    private static void startApp() {
        Scanner scanner = new Scanner(System.in);
        for ( ; ; ) {
            System.out.print("> ");
            if (scanner.hasNext()) {
                try {
                    // TODO: improve this code
                    System.out.print("> ");
                    String line = scanner.nextLine();
                    System.out.println();
                    String[] parts = line.split(" ");
                    Method method = Method.valueOf(parts[0]);
                    Optional<Path> path = Path.of(parts[1]);
                    if (path.isPresent()) {
                        RouteResponse response = router.executeRoute(method, path.get(), null);
                        response.getView().render();
                    }
                } catch (IllegalArgumentException ignored) {
                    System.out.println("Method not allowed");
                }
            }
        }
    }

    static DataSource getDataSource(String connectionUrl) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(connectionUrl);
        return dataSource;
    }

}
