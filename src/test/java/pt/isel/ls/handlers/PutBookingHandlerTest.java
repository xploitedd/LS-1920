package pt.isel.ls.handlers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;

import pt.isel.ls.router.request.Parameter;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.IdentifierView;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

import static pt.isel.ls.handlers.HandlersTestUtils.routeResponseEquals;

public class PutBookingHandlerTest {
    private static final DataSource dSource = DatasourceUtils.getDataSource();
    private static final int uid = 1;
    private static final int rid = 1;
    private static final Timestamp bTime = Timestamp.valueOf(LocalDateTime.of(2020,3,4,19,20));
    private static final Timestamp eTime = Timestamp.valueOf(LocalDateTime.of(2020,3,4,20,50));

    private static final String rname = "TestLabel";
    private static final String location = "ISEL";
    private static final String capacity = "250";

    private static final String uname = "Egas";
    private static final String mail = "Egas@Moniz.com";

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        DatasourceUtils.executeFile(dSource, "src/test/resources/sql/CreateTables.sql");
        Connection conn = dSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO room (name, location, capacity) VALUES (?, ?, ?);");

        stmt.setString(1, rname);
        stmt.setString(2, location);
        stmt.setInt(3, Integer.parseInt(capacity));
        stmt.execute();

        PreparedStatement ustmt = conn.prepareStatement("INSERT INTO \"user\" (email, name) VALUES (? , ?);");
        ustmt.setString(1,mail);
        ustmt.setString(2,uname);
        ustmt.execute();

        PreparedStatement bstmt = conn.prepareStatement(
                "INSERT INTO booking (begin, \"end\", rid, uid) VALUES (?, ?, ?, ?);"
        );

        bstmt.setTimestamp(1, bTime);
        bstmt.setTimestamp(2, eTime);
        bstmt.setInt(3, rid);
        bstmt.setInt(4, uid);
        bstmt.execute();
        conn.close();
    }

    @Test
    public void testExecute() throws RouteException {
        long b = Timestamp.valueOf(LocalDateTime.of(2020,12,12,10,10)).getTime();
        long d = Timestamp.valueOf(LocalDateTime.of(2020,12,12,10,50)).getTime();

        HashMap<String, Parameter> map = new HashMap<>();
        map.put("rid", new Parameter("1"));
        map.put("bid", new Parameter("1"));

        RouteRequest testRequest = RouteRequest.of("PUT /rooms/1/bookings/1 uid=1&begin=" + b + "&duration=" + d);
        testRequest.setPathParameters(map);

        RouteResponse result = new PutBookingHandler(new ConnectionProvider(dSource))
                .execute(testRequest);

        RouteResponse expected = new RouteResponse(new IdentifierView("Updated booking",1));
        Assert.assertTrue(routeResponseEquals(expected,result));
    }
}
