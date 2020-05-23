package pt.isel.ls.handlers.booking;

import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.sql.queries.UserQueries;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class PostBookingHandlerTest {

    private static Router router;
    private LocalDateTime begin2h;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        begin2h = LocalDateTime.now().plusHours(2).withMinute(10);
        provider.execute(conn -> {
            UserQueries userQueries = new UserQueries(conn);
            userQueries.createNewUser("teste", "teste@teste.com");

            RoomQueries roomQueries = new RoomQueries(conn);
            roomQueries.createNewRoom("teste", "teste", 10, "teste room",
                    new LinkedList<>());

            roomQueries.createNewRoom("teste2", "teste2", 10, "teste2 room",
                    new LinkedList<>());

            return null;
        });

        PostBookingHandler pbh = new PostBookingHandler(provider);
        router = new Router();
        router.registerRoute(pbh);
    }

    @Test
    public void createNewBooking() throws RouteException {
        long begin = Timestamp.valueOf(begin2h).getTime();

        RouteRequest request = RouteRequest.of(
                "POST /rooms/1/bookings uid=1&begin=" + begin + "&duration=10");

        router.getHandler(request).execute(request);
    }

    @Test(expected = RouteException.class)
    public void createNewInvalidBooking() throws RouteException {
        long begin = Timestamp.valueOf(begin2h).getTime();

        RouteRequest request = RouteRequest.of(
                "POST /rooms/1/bookings uid=1&begin=" + begin + "&duration=5");

        router.getHandler(request).execute(request);
    }

    @Test(expected = RouteException.class)
    public void createNewOutdatedBooking() throws RouteException {
        long begin = Timestamp.valueOf(LocalDateTime.of(
                2020, 5, 21, 12, 10
        )).getTime();

        RouteRequest request = RouteRequest.of(
                "POST /rooms/1/bookings uid=1&begin=" + begin + "&duration=10");

        router.getHandler(request).execute(request);
    }

    @Test(expected = RouteException.class)
    public void createNewOverlappingBooking_1() throws RouteException {
        long begin1 = Timestamp.valueOf(begin2h).getTime();

        RouteRequest request1 = RouteRequest.of(
                "POST /rooms/1/bookings uid=1&begin=" + begin1 + "&duration=10");

        router.getHandler(request1).execute(request1);

        long begin2 = Timestamp.valueOf(begin2h.minusMinutes(10)).getTime();

        RouteRequest request2 = RouteRequest.of(
                "POST /rooms/1/bookings uid=1&begin=" + begin2 + "&duration=20");

        router.getHandler(request2).execute(request2);
    }

    @Test
    public void createNewNoOverlappingBooking_1() throws RouteException {
        long begin1 = Timestamp.valueOf(begin2h).getTime();

        RouteRequest request1 = RouteRequest.of(
                "POST /rooms/1/bookings uid=1&begin=" + begin1 + "&duration=10");

        router.getHandler(request1).execute(request1);

        long begin2 = Timestamp.valueOf(begin2h.minusMinutes(10)).getTime();

        RouteRequest request2 = RouteRequest.of(
                "POST /rooms/1/bookings uid=1&begin=" + begin2 + "&duration=10");

        router.getHandler(request2).execute(request2);
    }

}
