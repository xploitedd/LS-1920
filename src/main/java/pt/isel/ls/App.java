package pt.isel.ls;

import java.io.PrintWriter;
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
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.router.RouteTemplate;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.view.console.RouteExceptionView;

public class App {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_DATABASE_URL";

    private DataSource dataSource;
    private Router router;

    private App() {
        String url = System.getenv(DATABASE_CONNECTION_ENV);
        if (url == null) {
            System.err.println("Please set the " + DATABASE_CONNECTION_ENV + " environment variable");
            System.exit(1);
        }

        dataSource = getDataSource(url);
        router = new Router();

        registerRoutes();
    }

    public static void main(String[] args) {
        App app = new App();
        app.startApp();
    }

    /**
     * Registers all the routes for this app
     */
    private void registerRoutes() {
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
        router.registerRoute(Method.GET, RouteTemplate.of("/users/{uid}?"),
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

    /**
     * Handles console app user interaction
     */
    private void startApp() {
        Scanner scanner = new Scanner(System.in);
        try (PrintWriter pw = new PrintWriter(System.out)) {
            // for needed where to print the user input character
            for ( ; ; ) {
                System.out.print("> ");
                if (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    RouteRequest request = RouteRequest.of(line);

                    RouteResponse response = router.getHandler(request)
                            .execute(request);

                    response.getView().render(pw);
                    pw.flush();
                }
            }
        } catch (RouteException e) {
            try (PrintWriter pw = new PrintWriter(System.out)) {
                new RouteExceptionView(e).render(pw);
            } catch (Exception ex) {
                // print unexpected exceptions
                e.printStackTrace();
            }
        } catch (Exception e) {
            // print unexpected exceptions
            e.printStackTrace();
        }
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
