package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

import pt.isel.ls.handlers.booking.DeleteBookingHandler;
import pt.isel.ls.handlers.booking.GetRoomBookingHandler;
import pt.isel.ls.handlers.booking.GetRoomBookingsHandler;
import pt.isel.ls.handlers.booking.PostBookingHandler;
import pt.isel.ls.handlers.booking.PutBookingHandler;
import pt.isel.ls.handlers.user.GetUserBookingsHandler;
import pt.isel.ls.handlers.misc.ExitHandler;
import pt.isel.ls.handlers.label.GetLabeledRoomsHandler;
import pt.isel.ls.handlers.label.GetLabelsHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.handlers.room.GetRoomsHandler;
import pt.isel.ls.handlers.misc.GetTimeHandler;
import pt.isel.ls.handlers.user.GetUserHandler;
import pt.isel.ls.handlers.misc.OptionHandler;
import pt.isel.ls.handlers.label.PostLabelHandler;
import pt.isel.ls.handlers.room.PostRoomHandler;
import pt.isel.ls.handlers.user.GetUsersHandler;
import pt.isel.ls.handlers.user.PostUserHandler;
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
        router.registerRoute(new ExitHandler());
        router.registerRoute(new GetTimeHandler());
        router.registerRoute(new OptionHandler(router));

        // Room Handlers
        router.registerRoute(new PostRoomHandler(connProvider));
        router.registerRoute(new GetRoomsHandler(connProvider));
        router.registerRoute(new GetRoomHandler(connProvider));

        // Booking Handlers
        router.registerRoute(new PostBookingHandler(connProvider));
        router.registerRoute(new GetRoomBookingHandler(connProvider));
        router.registerRoute(new GetRoomBookingsHandler(connProvider));
        router.registerRoute(new PutBookingHandler(connProvider));
        router.registerRoute(new DeleteBookingHandler(connProvider));

        // User Handlers
        router.registerRoute(new PostUserHandler(connProvider));
        router.registerRoute(new GetUsersHandler(connProvider));
        router.registerRoute(new GetUserHandler(connProvider));
        router.registerRoute(new GetUserBookingsHandler(connProvider));

        // Label Handlers
        router.registerRoute(new PostLabelHandler(connProvider));
        router.registerRoute(new GetLabelsHandler(connProvider));
        router.registerRoute(new GetLabeledRoomsHandler(connProvider));
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
