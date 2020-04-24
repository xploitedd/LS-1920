package pt.isel.ls.handlers.booking;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.User;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.IdentifierView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class DeleteBookingHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");

        Timestamp begin = Timestamp.valueOf(LocalDateTime.of(2020, 4, 4, 10, 10));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(2020, 4, 5, 10, 10));
        provider.execute(conn -> {
            UserQueries userQueries = new UserQueries(conn);
            User u1 = userQueries.createNewUser("teste", "teste@teste.com");

            RoomQueries roomQueries = new RoomQueries(conn);
            Room r1 = roomQueries.createNewRoom("teste", "teste", 10, "teste room",
                    new LinkedList<>());

            BookingQueries bookingQueries = new BookingQueries(conn);
            bookingQueries.createNewBooking(r1.getRid(), u1.getUid(), begin, end);

            return null;
        });

        DeleteBookingHandler dbh = new DeleteBookingHandler(provider);
        router = new Router();
        router.registerRoute(dbh);
    }

    @Test
    public void deleteBooking() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "DELETE /rooms/1/bookings/1");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertTrue(response.getView() instanceof IdentifierView);
    }

    @Test
    public void deleteInvalidBooking() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "DELETE /rooms/1/bookings/2");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertFalse(response.getView() instanceof IdentifierView);
    }

}
