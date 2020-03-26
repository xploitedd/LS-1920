package pt.isel.ls.queries;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.TestDatasource;
import pt.isel.ls.model.Label;
import pt.isel.ls.sql.queries.LabelQueries;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.DatabaseTest.executeFile;

public class LabelQueriesTest {

    private static final DataSource dSource = TestDatasource.getDataSource();

    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        executeFile("src/test/sql/CreateTables.sql");
    }

    @Test
    public void testCreateNewLabel() throws Throwable {
        Connection conn = dSource.getConnection();
        LabelQueries query = new LabelQueries(conn);

        final String lName = "TestLabel";

        query.createNewLabel(lName);

        PreparedStatement stmt = conn.prepareStatement("SELECT name FROM label WHERE name = ?;");
        stmt.setString(1,lName);
        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            Assert.assertEquals(lName, res.getString(1));
        } else {
            Assert.fail("Returned Null");
        }

    }

    @Test
    public void testGetLabel() throws Throwable {
        Connection conn = dSource.getConnection();

        final String lName = "LabelsGetTest";

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, lName);
        stmt.execute();

        LabelQueries query = new LabelQueries(conn);
        Label test = query.getLabel(lName);

        PreparedStatement qstmt = conn.prepareStatement("SELECT lid FROM label WHERE name = ?;");
        qstmt.setString(1, lName);
        ResultSet res = qstmt.executeQuery();

        if (res.next()) {
            Assert.assertEquals(lName, test.getName());
            Assert.assertEquals(res.getInt(1), test.getLid());
        } else {
            Assert.fail("Did Not Insert");
        }
    }

    @Test
    public void testGetLabels() throws Throwable {
        Connection conn = dSource.getConnection();

        final String lName = "LabelGetTest";

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, lName);
        stmt.execute();

        LabelQueries query = new LabelQueries(conn);
        Iterable<Label> iter = query.getLabels();

        for (Label lbl: iter) {
            Assert.assertNotNull(lbl.getLid());
            Assert.assertNotNull(lbl.getName());
        }

    }
}
