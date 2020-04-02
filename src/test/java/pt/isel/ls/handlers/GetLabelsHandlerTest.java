package pt.isel.ls.handlers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.response.RouteException;
import pt.isel.ls.router.response.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.TableView;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.handlers.HandlersTestUtils.routeResponseEquals;

public class GetLabelsHandlerTest {

    private static final DataSource dSource = DatasourceUtils.getDataSource();
    private final String name = "TestLabel";
    private final String lid = "1";

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        DatasourceUtils.executeFile(dSource, "src/test/resources/sql/CreateTables.sql");
    }

    @Test
    public void testExecute() throws SQLException, RouteException {
        Table table = new Table("Label Id", "Name");
        table.addTableRow(lid, name);

        Connection conn = dSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1,name);
        stmt.execute();

        conn.close();

        RouteResponse result = new GetLabelsHandler(new ConnectionProvider(dSource))
                .execute(null);

        RouteResponse expected = new RouteResponse(new TableView(table));
        Assert.assertTrue(routeResponseEquals(expected,result));

    }

}
