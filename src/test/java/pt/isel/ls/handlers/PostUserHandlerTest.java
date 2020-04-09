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

public class PostUserHandlerTest {

    private static final DataSource dSource = DatasourceUtils.getDataSource();

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        DatasourceUtils.executeFile(dSource, "src/test/resources/sql/CreateTables.sql");
    }

    @Test
    public void testExecute() throws RouteException, IOException {
        RouteResponse expected = new RouteResponse(new IdentifierView("user",1));

        RouteResponse result = new PostUserHandler(new ConnectionProvider(dSource))
                .execute(RouteRequest.of("POST /user name=testUser&email=test@user.post"));

        Assert.assertTrue(routeResponseEquals(expected,result));
    }
}
