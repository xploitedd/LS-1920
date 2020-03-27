package pt.isel.ls.handlers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.TestDatasource;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.Parameter;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.console.TableView;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import static pt.isel.ls.DatabaseTest.executeFile;
import static pt.isel.ls.handlers.HandlersTestUtils.routeResponseEquals;

public class GetLabeledRoomsHandlerTest {

    private static final DataSource dSource = TestDatasource.getDataSource();
    private static final String name = "TestLabel";
    private static final String lid = "1";

    private static final int rid = 1;
    private static final String rname = "TestLabel";
    private static final String location = "ISEL";
    private static final String capacity = "250";
    private static final String desc = "No Description";

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        executeFile("src/test/sql/CreateTables.sql");

        Connection conn = dSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1,name);
        stmt.execute();

        PreparedStatement lstmt = conn.prepareStatement(
                "INSERT INTO room (name, location, capacity) VALUES (?, ?, ?);");

        lstmt.setString(1, rname);
        lstmt.setString(2, location);
        lstmt.setInt(3, Integer.parseInt(capacity));
        lstmt.execute();

        PreparedStatement rl = conn.prepareStatement(
                "INSERT INTO room_label (lid, rid) VALUES (?, ?);"
        );

        rl.setInt(1, 1);
        rl.setInt(2, 1);
        rl.execute();
    }

    @Test
    public void testExecute() throws SQLException, RouteException {

        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");
        table.addTableRow(Integer.toString(rid), name, location, capacity, desc);

        RouteResponse expected = new RouteResponse(new TableView(table));

        HashMap<String, Parameter> map = new HashMap<>();
        map.put("lid", new Parameter("1"));

        RouteRequest testRequest = RouteRequest.of("GET /labels/1/rooms)");
        testRequest.setPathParameters(map);
        RouteResponse result = new GetLabeledRoomsHandler(dSource)
                .execute(testRequest);

        Assert.assertTrue(routeResponseEquals(expected,result));
    }

}
