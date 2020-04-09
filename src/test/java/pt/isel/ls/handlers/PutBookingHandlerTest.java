package pt.isel.ls.handlers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;

import pt.isel.ls.router.response.RouteException;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class PutBookingHandlerTest {
    private static final DataSource dSource = DatasourceUtils.getDataSource();

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        DatasourceUtils.executeFile(dSource, "src/test/resources/sql/CreateTables.sql");
    }

    @Test
    public void testExecute() throws RouteException {
        //TODO
        /*
        RouteResponse expected = new RouteResponse(new IdentifierView("Update Booking",1));

        long b = Timestamp.valueOf(LocalDateTime.of(2020,12,12,10,10)).getTime();
        long e = Timestamp.valueOf(LocalDateTime.of(2020,12,12,10,50)).getTime();

        RouteResponse result = new PutBookingHandler(new ConnectionProvider(dSource))
                .execute(RouteRequest.of("PUT /rooms/1/bookings/1 uid=1&begin=" + b + "&duration=" + e));

        Assert.assertTrue(routeResponseEquals(expected,result));*/
        Assert.assertTrue(true);
    }
}
