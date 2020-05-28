package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.app.ConsoleApplication;
import pt.isel.ls.app.http.HttpPool;
import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.handlers.booking.DeleteBookingHandler;
import pt.isel.ls.handlers.booking.GetRoomBookingHandler;
import pt.isel.ls.handlers.booking.GetRoomBookingsHandler;
import pt.isel.ls.handlers.booking.GetUserBookingsHandler;
import pt.isel.ls.handlers.booking.PostBookingHandler;
import pt.isel.ls.handlers.booking.PutBookingHandler;
import pt.isel.ls.handlers.label.GetLabelHandler;
import pt.isel.ls.handlers.label.GetLabeledRoomsHandler;
import pt.isel.ls.handlers.label.GetLabelsHandler;
import pt.isel.ls.handlers.misc.CloseHandler;
import pt.isel.ls.handlers.misc.ExitHandler;
import pt.isel.ls.handlers.misc.GetHomeHandler;
import pt.isel.ls.handlers.misc.GetTimeHandler;
import pt.isel.ls.handlers.misc.ListenHandler;
import pt.isel.ls.handlers.misc.OptionHandler;
import pt.isel.ls.handlers.room.GetRoomCreateHandler;
import pt.isel.ls.handlers.room.GetRoomHandler;
import pt.isel.ls.handlers.room.GetRoomSearchHandler;
import pt.isel.ls.handlers.room.GetRoomsHandler;
import pt.isel.ls.handlers.room.PostRoomHandler;
import pt.isel.ls.handlers.user.GetUserHandler;
import pt.isel.ls.handlers.label.PostLabelHandler;
import pt.isel.ls.handlers.user.GetUsersHandler;
import pt.isel.ls.handlers.user.PostUserHandler;
import pt.isel.ls.router.Router;
import pt.isel.ls.sql.ConnectionProvider;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final String DATABASE_CONNECTION_ENV = "JDBC_DATABASE_URL";

    private final ConnectionProvider connProvider;
    private final Router router;
    private final HttpPool httpPool;

    private App() {
        String url = System.getenv(DATABASE_CONNECTION_ENV);
        if (url == null) {
            LOGGER.error("Database environment variable not set!");
            throw new AppException("Please set the " + DATABASE_CONNECTION_ENV + " environment variable");
        }

        this.connProvider = new ConnectionProvider(getDataSource(url));
        this.router = new Router();
        this.httpPool = new HttpPool();

        registerRoutes();
    }

    public static void main(String[] args) {
        try {
            App app = new App();
            app.run(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run(String[] args) {
        ConsoleApplication consoleApp = new ConsoleApplication(router);
        if (args.length >= 2) {
            LOGGER.debug("application in argument mode");
            // process single command provided in the arguments
            consoleApp.processInput(String.join(" ", args));
        } else {
            LOGGER.debug("application in interactive mode");
            // run interactive mode
            consoleApp.run();
        }
    }

    /**
     * Registers all the routes for this app
     */
    private void registerRoutes() {
        // Register All Routes
        router.registerRoute(new ExitHandler(httpPool));
        router.registerRoute(new GetTimeHandler());
        router.registerRoute(new OptionHandler(router));
        router.registerRoute(new ListenHandler(router, httpPool));
        router.registerRoute(new CloseHandler(httpPool));
        router.registerRoute(new GetHomeHandler());

        // Room Handlers
        router.registerRoute(new PostRoomHandler(connProvider));
        router.registerRoute(new GetRoomsHandler(connProvider));
        router.registerRoute(new GetRoomHandler(connProvider));
        router.registerRoute(new GetRoomSearchHandler(connProvider));
        router.registerRoute(new GetRoomCreateHandler(connProvider));

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
        router.registerRoute(new GetLabelHandler(connProvider));
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
