package pt.isel.ls.handlers.room;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.model.User;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.sql.queries.UserQueries;
import pt.isel.ls.view.TableView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class GetRoomsHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        LinkedList<Label> labelList = new LinkedList<>();
        Timestamp begin = Timestamp.valueOf(LocalDateTime.of(2020, 4, 4, 10, 10));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(2021, 4, 4, 10, 10));
        provider.execute(conn -> {
            LabelQueries labelQueries = new LabelQueries(conn);
            Label l1 = labelQueries.createNewLabel("teste1");
            labelList.add(l1);

            RoomQueries roomQueries = new RoomQueries(conn);
            roomQueries.createNewRoom("TR1", "TL1", 32, null, new LinkedList<>());
            roomQueries.createNewRoom("TR2","TL1", 24, null, labelList);
            Room r3 = roomQueries.createNewRoom("TR3","TL2", 16, "TD", new LinkedList<>());

            UserQueries userQueries = new UserQueries(conn);
            User u1 = userQueries.createNewUser("teste", "teste@teste.com");

            BookingQueries bookingQueries = new BookingQueries(conn);
            bookingQueries.createNewBooking(r3.getRid(), u1.getUid(), begin, end);
            return null;
        });

        GetRoomsHandler grh = new GetRoomsHandler(provider);
        router = new Router();
        router.registerRoute(grh);
    }

    @Test
    public void getAllRooms() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "GET /rooms");

        HandlerResponse response = router.getHandler(request).execute(request);
        TableView tableView = (TableView) response.getView();

        Assert.assertEquals(3, tableView.getTable().getRowCount());
    }

    @Test
    public void getRoomsByTime1() throws RouteException {
        Timestamp begin = Timestamp.valueOf(LocalDateTime.of(2020, 4, 5, 10, 10));

        //Well inside the booking
        RouteRequest request = RouteRequest.of(
                "GET /rooms begin=" + begin.getTime() + "&duration=49980");

        HandlerResponse response = router.getHandler(request).execute(request);
        TableView tableView = (TableView) response.getView();

        Assert.assertEquals(2, tableView.getTable().getRowCount());
    }

    @Test
    public void getRoomsByTime2() throws RouteException {
        Timestamp begin = Timestamp.valueOf(LocalDateTime.of(2020, 4, 4, 10, 10));
        //same start as booking, different end
        RouteRequest request = RouteRequest.of(
                "GET /rooms begin=" + begin.getTime() + "&duration=49980");

        HandlerResponse response = router.getHandler(request).execute(request);
        TableView tableView = (TableView) response.getView();

        Assert.assertEquals(2, tableView.getTable().getRowCount());
    }

    @Test
    public void getRoomsByTime3() throws RouteException {
        Timestamp begin = Timestamp.valueOf(LocalDateTime.of(2021, 4, 4, 10, 0));
        //same end as booking, different start
        RouteRequest request = RouteRequest.of(
                "GET /rooms begin=" + begin.getTime() + "&duration=10");

        HandlerResponse response = router.getHandler(request).execute(request);
        TableView tableView = (TableView) response.getView();

        Assert.assertEquals(2, tableView.getTable().getRowCount());
    }

    @Test
    public void getRoomsByTime4() throws RouteException {
        Timestamp begin = Timestamp.valueOf(LocalDateTime.of(2020, 3, 4, 10, 0));
        //before booking, should be valid
        RouteRequest request = RouteRequest.of(
                "GET /rooms begin=" + begin.getTime() + "&duration=10");

        HandlerResponse response = router.getHandler(request).execute(request);
        TableView tableView = (TableView) response.getView();

        Assert.assertEquals(3, tableView.getTable().getRowCount());
    }


    @Test
    public void getRoomsByCapacity() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "GET /rooms capacity=25");

        HandlerResponse response = router.getHandler(request).execute(request);
        TableView tableView = (TableView) response.getView();

        Assert.assertEquals(1, tableView.getTable().getRowCount());
    }

    @Test
    public void getRoomsByLabel() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "GET /rooms label=teste1");

        HandlerResponse response = router.getHandler(request).execute(request);
        TableView tableView = (TableView) response.getView();

        Assert.assertEquals(1, tableView.getTable().getRowCount());
    }

}