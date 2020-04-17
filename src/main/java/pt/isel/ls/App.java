package pt.isel.ls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
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
import pt.isel.ls.router.request.HeaderType;
import pt.isel.ls.router.request.Method;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.RouteTemplate;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.ExceptionView;
import pt.isel.ls.view.ViewType;

public class App {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_DATABASE_URL";

    private ConnectionProvider connProvider;
    private Router router;

    private App() {
        String url = System.getenv(DATABASE_CONNECTION_ENV);
        if (url == null) {
            System.err.println("Please set the " + DATABASE_CONNECTION_ENV + " environment variable");
            System.exit(1);
        }

        this.connProvider = new ConnectionProvider(getDataSource(url));
        this.router = new Router();

        registerRoutes();
    }

    public static void main(String[] args) {
        App app = new App();

        try {
            app.startApp();
        } catch (IOException e) {
            // handle unexpected exceptions
            e.printStackTrace();
        }
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
     * Handles console app user interaction
     */
    private void startApp() throws IOException {
        Scanner scanner = new Scanner(System.in);
        // for needed where to print the user input character
        for ( ; ; ) {
            System.out.print("> ");
            if (scanner.hasNext()) {
                String line = scanner.nextLine();
                // TODO: optimize this code and cleanup
                OutputStream output = System.out;
                ViewType viewType = null;
                try {
                    RouteRequest request = RouteRequest.of(line);
                    viewType = ViewType.of(request.getHeaderValue(HeaderType.Accept)
                            .orElse(null));

                    output = request.getHeaderValue(HeaderType.FileName)
                            .map(f -> {
                                try {
                                    return new FileOutputStream(f);
                                } catch (FileNotFoundException e) {
                                    return System.out;
                                }
                            }).orElse(System.out);

                    PrintWriter pw = new PrintWriter(output);
                    HandlerResponse response = router.getHandler(request)
                            .execute(request);

                    response.getView().render(viewType, pw);
                    pw.flush();
                } catch (RouteException | IOException e) {
                    PrintWriter pw = new PrintWriter(output);
                    new ExceptionView(e).render(viewType, pw);
                    pw.flush();
                }
            }
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
