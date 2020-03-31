package pt.isel.ls.handlers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.model.Table;
import pt.isel.ls.router.RouteException;
import pt.isel.ls.router.RouteRequest;
import pt.isel.ls.router.RouteResponse;
import pt.isel.ls.sql.ConnectionProvider;
import pt.isel.ls.view.TableView;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.handlers.HandlersTestUtils.routeResponseEquals;

public class GetUserHandlerTest {
    private static final DataSource dSource = DatasourceUtils.getDataSource();
    private static final String uid = "1";
    private static final String name = "Egas";
    private static final String mail = "Egas@Moniz.com";

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        DatasourceUtils.executeFile(dSource, "src/test/resources/sql/CreateTables.sql");
        Connection conn = dSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO \"user\" (email, name) VALUES (? , ?);");
        stmt.setString(1,mail);
        stmt.setString(2,name);
        stmt.execute();
        conn.close();
    }

    @Test
    public void testExecuteGetUsers() throws RouteException {
        Table table = new Table("User Id", "Name", "Email");
        table.addTableRow(uid, name, mail);

        RouteResponse expected = new RouteResponse(new TableView(table));

        RouteResponse result = new GetUserHandler(new ConnectionProvider(dSource))
                .execute(RouteRequest.of("GET /users"));

        Assert.assertTrue(routeResponseEquals(expected,result));
    }

    @Test
    public void testExecuteGetUser() throws RouteException {
        Table table = new Table("User Id", "Name", "Email");
        table.addTableRow(uid, name, mail);

        RouteResponse expected = new RouteResponse(new TableView(table));

        RouteResponse result = new GetUserHandler(new ConnectionProvider(dSource))
                .execute(RouteRequest.of("GET /users/1"));

        Assert.assertTrue(routeResponseEquals(expected,result));
    }

}
