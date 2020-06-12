package pt.isel.ls.handlers.room;

import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.exceptions.parameter.ValidatorException;
import pt.isel.ls.router.Router;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.sql.queries.LabelQueries;

public class PostRoomHandlerTest {

    private static Router router;

    @Before
    public void beforeEach() throws RouteException {
        ConnectionProvider provider = new ConnectionProvider(DatasourceUtils.getDataSource());
        DatasourceUtils.executeFile("CreateTables.sql");
        provider.execute(conn -> {
            LabelQueries labelQueries = new LabelQueries(conn);
            labelQueries.createNewLabel("teste1");

            return null;
        });

        PostRoomHandler prh = new PostRoomHandler(provider);
        router = new Router();
        router.registerRoute(prh);
    }

    @Test
    public void createNewRoomWithoutDesc() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "POST /rooms name=TR1&location=TL1&capacity=32");

        router.getHandler(request).execute(request);
    }

    @Test
    public void createNewRoomWithDesc() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "POST /rooms name=TR1&location=TL1&capacity=32&description=Test+Description+1");

        router.getHandler(request).execute(request);
    }

    @Test
    public void createNewRoomWithLabel() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "POST /rooms name=TR1&location=TL1&capacity=32&label=teste1");

        router.getHandler(request).execute(request);
    }

    @Test(expected = ValidatorException.class)
    public void createNewInvalidRoom() throws RouteException {

        RouteRequest request = RouteRequest.of(
                "POST /rooms name=TR1&location=TL1&capacity=0");

        router.getHandler(request).execute(request);
    }

    @Test(expected = ValidatorException.class)
    public void createNewOverlappingRoom() throws RouteException {

        RouteRequest request1 = RouteRequest.of(
                "POST /rooms name=TR1&location=TL1&capacity=32");

        router.getHandler(request1).execute(request1);

        RouteRequest request2 = RouteRequest.of(
                "POST /rooms name=TR1&location=TL1&capacity=32");

        router.getHandler(request2).execute(request2);
    }

}
