package pt.isel.ls.handlers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.request.RouteRequest;
import pt.isel.ls.router.response.HandlerResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.TableView;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.handlers.HandlersTestUtils.routeResponseEquals;

public class GetRoomsHandlerTest {
    private static final DataSource dSource = DatasourceUtils.getDataSource();
    private static final String name = "TestLabel";
    private static final String rid = "1";
    private static final String location = "ISEL";
    private static final String capacity = "250";
    private static final String desc = "No Description";

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        DatasourceUtils.executeFile(dSource, "src/test/resources/sql/CreateTables.sql");
        Connection conn = dSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO room (name, location, capacity) VALUES (?, ?, ?);");

        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setInt(3, Integer.parseInt(capacity));
        stmt.execute();
        conn.close();
    }

    @Test
    public void testExecuteAllRooms() throws RouteException, IOException {
        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");
        table.addTableRow(rid, name, location, capacity, desc);

        HandlerResponse expected = new HandlerResponse(new TableView(table));

        HandlerResponse result = new GetRoomsHandler(new ConnectionProvider(dSource))
                .execute(RouteRequest.of("GET /rooms"));

        Assert.assertTrue(routeResponseEquals(expected,result));
    }

    @Test
    public void testExecuteGetRoom() throws RouteException, IOException {
        Table table = new Table("RID", "Name", "Location", "Capacity", "Description");
        table.addTableRow(rid, name, location, capacity, desc);

        HandlerResponse expected = new HandlerResponse(new TableView(table));

        HandlerResponse result = new GetRoomsHandler(new ConnectionProvider(dSource))
                .execute(RouteRequest.of("GET /rooms/1"));

        Assert.assertTrue(routeResponseEquals(expected,result));
    }
}
