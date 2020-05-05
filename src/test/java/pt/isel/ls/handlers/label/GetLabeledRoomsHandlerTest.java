package pt.isel.ls.handlers.label;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.model.Label;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.view.TableView;

import java.util.LinkedList;
import java.util.List;

public class GetLabeledRoomsHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        provider.execute(conn -> {
            LabelQueries labelQueries = new LabelQueries(conn);
            labelQueries.createNewLabel("teste1");

            RoomQueries roomQueries = new RoomQueries(conn);
            List<Label> labels = new LinkedList<>();
            labels.add(labelQueries.getLabel("teste1"));
            roomQueries.createNewRoom("teste", "teste", 10, "teste room", labels);

            return null;
        });

        GetLabeledRoomsHandler pbh = new GetLabeledRoomsHandler(provider);
        router = new Router();
        router.registerRoute(pbh);
    }

    @Test
    public void getLabeledRooms() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "GET /labels/" + 1 + "/rooms");

        HandlerResponse response = router.getHandler(request).execute(request);
        Assert.assertTrue(response.getView() instanceof TableView);
    }

    @Test(expected = RouteException.class)
    public void getLabeledRoomThatDoesNotExist() throws RouteException {
        RouteRequest request = RouteRequest.of(
                "GET /labels/2/rooms");

        router.getHandler(request).execute(request);
    }
}
