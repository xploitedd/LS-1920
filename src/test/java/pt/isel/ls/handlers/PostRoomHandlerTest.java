package pt.isel.ls.handlers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.IdentifierView;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static pt.isel.ls.handlers.HandlersTestUtils.routeResponseEquals;

public class PostRoomHandlerTest {

    private static final DataSource dSource = DatasourceUtils.getDataSource();

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        DatasourceUtils.executeFile(dSource, "src/test/resources/sql/CreateTables.sql");
    }

    @Test
    public void testExecute() throws RouteException, IOException {
        RouteResponse expected = new RouteResponse(new IdentifierView("room",1));

        RouteResponse result = new PostRoomHandler(new ConnectionProvider(dSource))
                .execute(RouteRequest.of("POST /rooms name=testRoom&location=testLocation&capacity=44"));

        Assert.assertTrue(routeResponseEquals(expected,result));
    }
}
