package pt.isel.ls.handlers.booking;

import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.User;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.sql.queries.UserQueries;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class PutBookingHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        LocalDateTime now = LocalDateTime.now();
        Timestamp begin = Timestamp.valueOf(LocalDateTime.of(
                now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 1, 10
        ));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(
                now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, now.getHour() + 1, 10
        ));
        provider.execute(conn -> {
            UserQueries userQueries = new UserQueries(conn);
            User u1 = userQueries.createNewUser("teste", "teste@teste.com");
            userQueries.createNewUser("teste2", "teste2@teste.com");

            RoomQueries roomQueries = new RoomQueries(conn);
            Room r1 = roomQueries.createNewRoom("teste", "teste", 10, "teste room",
                    new LinkedList<>());

            BookingQueries bookingQueries = new BookingQueries(conn);
            bookingQueries.createNewBooking(r1.getRid(), u1.getUid(), begin, end);

            return null;
        });

        PutBookingHandler pbh = new PutBookingHandler(provider);
        router = new Router();
        router.registerRoute(pbh);
    }

    @Test
    public void modifyBooking() throws RouteException {
        LocalDateTime now = LocalDateTime.now();
        long begin = Timestamp.valueOf(LocalDateTime.of(
                now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 1, 10
        )).getTime();

        RouteRequest request = RouteRequest.of(
                "PUT /rooms/1/bookings/1 uid=2&begin=" + begin + "&duration=10");

        router.getHandler(request).execute(router, request);
    }

    @Test(expected = RouteException.class)
    public void modifyInvalidBooking() throws RouteException {
        LocalDateTime now = LocalDateTime.now();
        long begin = Timestamp.valueOf(LocalDateTime.of(
                now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 1, 10
        )).getTime();

        RouteRequest request = RouteRequest.of(
                "PUT /rooms/1/bookings/2 uid=2&begin=" + begin + "&duration=10");

        router.getHandler(request).execute(router, request);
    }

}
