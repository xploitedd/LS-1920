package pt.isel.ls.handlers;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.TestDatasource;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.view.console.TableView;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.DatabaseTest.executeFile;

public class GetLabelsHandlerTest {

    private static final DataSource dSource = TestDatasource.getDataSource();
    private final String name = "TestLabel";
    private final String lid = "1";

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        executeFile("src/test/sql/CreateTables.sql");
    }

    @Test
    public void testExecute() throws SQLException, RouteException {

        Table table = new Table("Label Id", "Name");
        table.addTableRow(lid, name);

        RouteResponse expected = new RouteResponse(new TableView(table));

        Connection conn = dSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1,name);
        stmt.execute();

        RouteResponse result = new GetLabelsHandler(dSource)
                .execute(null);

        //Assert.assertEquals(expected, result);
    }

}
