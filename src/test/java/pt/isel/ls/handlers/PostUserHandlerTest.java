package pt.isel.ls.handlers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.TestDatasource;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.console.IdentifierView;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.DatabaseTest.executeFile;
import static pt.isel.ls.handlers.HandlersTestUtils.routeResponseEquals;

public class PostUserHandlerTest {

    private static final DataSource dSource = TestDatasource.getDataSource();

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        executeFile("src/test/sql/CreateTables.sql");
    }

    @Test
    public void testExecute() throws SQLException, RouteException {
        RouteResponse expected = new RouteResponse(new IdentifierView("user",1));

        Connection conn = dSource.getConnection();

        RouteResponse result = new PostUserHandler(dSource)
                .execute(RouteRequest.of("POST /user name=testUser&email=test@user.get"));

        Assert.assertTrue(routeResponseEquals(expected,result));
    }
}
