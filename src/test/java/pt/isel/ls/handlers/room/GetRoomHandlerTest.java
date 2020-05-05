package pt.isel.ls.handlers.room;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.TableView;

import java.util.LinkedList;

public class GetRoomHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        provider.execute(conn -> {
            LabelQueries labelQueries = new LabelQueries(conn);
            labelQueries.createNewLabel("teste1");

            RoomQueries roomQueries = new RoomQueries(conn);
            roomQueries.createNewRoom("TR1", "TL1", 32, null, new LinkedList<>());


            return null;
        });

        GetRoomHandler grh = new GetRoomHandler(provider);
        router = new Router();
        router.registerRoute(grh);
    }

    @Test
    public void getRoom() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "GET /rooms/1");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertTrue(response.getView() instanceof TableView);
    }

    @Test(expected = RouteException.class)
    public void getRoomDoesNotExist() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "GET /rooms/2");

        router.getHandler(request).execute(request);
    }
}