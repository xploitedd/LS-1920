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
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.booking.RoomBookingsView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class GetRoomBookingsHandlerTest {

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

            RoomQueries roomQueries = new RoomQueries(conn);
            Room r1 = roomQueries.createNewRoom("teste", "teste", 10, "teste room",
                    new LinkedList<>());
            roomQueries.createNewRoom("teste2", "teste2", 12, "teste room",
                    new LinkedList<>());

            BookingQueries bookingQueries = new BookingQueries(conn);
            bookingQueries.createNewBooking(r1.getRid(), u1.getUid(), begin, end);

            return null;
        });

        GetRoomBookingsHandler grbh = new GetRoomBookingsHandler(provider);
        router = new Router();
        router.registerRoute(grbh);
    }

    @Test
    public void getRoomBookings() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "GET /rooms/1/bookings");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertTrue(response.getView() instanceof RoomBookingsView);
        //TableView tableView = (TableView) response.getView();
        //Assert.assertEquals(1, tableView.getTable().getRowCount());
    }

    @Test
    public void getEmptyRoomBooking() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "GET /rooms/2/bookings");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertTrue(response.getView() instanceof RoomBookingsView);
        //TableView tableView = (TableView) response.getView();
        //Assert.assertEquals(0, tableView.getTable().getRowCount());
    }

}
