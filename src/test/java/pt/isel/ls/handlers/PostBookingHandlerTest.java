package pt.isel.ls.handlers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.IdentifierView;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

import static pt.isel.ls.handlers.HandlersTestUtils.routeResponseEquals;

public class PostBookingHandlerTest {

    private static final DataSource dSource = DatasourceUtils.getDataSource();

    @BeforeClass
    public static void resetTables() throws SQLException, IOException, RouteException {
        DatasourceUtils.executeFile(dSource, "src/test/resources/sql/CreateTables.sql");
        new PostRoomHandler(new ConnectionProvider(dSource))
                .execute(RouteRequest.of("POST /room name=testRoom&location=testLocation&capacity=44"));
        new PostUserHandler(new ConnectionProvider(dSource))
                .execute(RouteRequest.of("POST /user name=testUser&email=test@user.post"));
    }

    @Test
    public void testExecute() throws RouteException {
        RouteResponse expected = new RouteResponse(new IdentifierView("booking",1));

        HashMap<String, Parameter> map = new HashMap<>();
        map.put("rid", new Parameter("1"));
        long b = Timestamp.valueOf(LocalDateTime.of(2020,12,12,10,10)).getTime();
        long e = Timestamp.valueOf(LocalDateTime.of(2020,12,12,10,50)).getTime();
        RouteRequest testRequest = RouteRequest.of("POST /rooms/1/bookings uid=1&begin=" + b + "&end=" + e);
        testRequest.setPathParameters(map);
        RouteResponse result = new PostBookingHandler(new ConnectionProvider(dSource))
                .execute(testRequest);

        Assert.assertTrue(routeResponseEquals(expected,result));
    }
}
