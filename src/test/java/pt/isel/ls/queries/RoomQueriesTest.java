package pt.isel.ls.queries;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.TestDatasource;
import pt.isel.ls.model.Room;
import pt.isel.ls.sql.queries.RoomQueries;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static pt.isel.ls.DatabaseTest.executeFile;

public class RoomQueriesTest {

    private static final DataSource dSource = TestDatasource.getDataSource();


    @BeforeClass
    public static void resetTables() throws SQLException, IOException {
        executeFile("src/test/sql/CreateTables.sql");
    }

    final String name = "TestRoom";
    final String location = "TestLocation";
    final int capacity = 32;
    //final String DESCRIPTION = "TestDescription";

    @Test
    public void testCreateNewRoom() throws Throwable {
        Connection conn = dSource.getConnection();
        RoomQueries query = new RoomQueries(conn);



        query.createNewRoom(name, location, capacity,null,new LinkedList<>());

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT rid, name, location, capacity FROM room"
                        + " WHERE name = ? AND location = ? AND capacity = ?"
        );
        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setInt(3, capacity);
        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            Assert.assertEquals(name, res.getString(2));
            Assert.assertEquals(location, res.getString(3));
        } else {
            Assert.fail("ResultSet is Empty");
        }
        conn.close();
    }

    @Test
    public void testGetRoomByNameLocationAndCapacity() throws Throwable {
        Connection conn = dSource.getConnection();
        RoomQueries query = new RoomQueries(conn);

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO room (name, location, capacity) VALUES (?, ?, ?);");
        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setInt(3, capacity);
        stmt.execute();

        Room test = query.getRoom(name, location, capacity);

        Assert.assertNotNull(test);
        Assert.assertEquals(name,test.getName());
        Assert.assertEquals(location, test.getLocation());
        Assert.assertEquals(capacity, test.getCapacity());
    }

    @Test
    public void testGetRoomById() throws Throwable {
        Connection conn = dSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO room (name, location, capacity) VALUES (?, ?, ?);");
        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setInt(3, capacity);
        stmt.execute();

        stmt = conn.prepareStatement(
                "SELECT rid FROM room WHERE name = ? AND location = ? AND capacity = ?"
        );
        stmt.setString(1, name);
        stmt.setString(2, location);
        stmt.setInt(3, capacity);
        ResultSet res = stmt.executeQuery();
        Assert.assertTrue(res.next());

        RoomQueries query = new RoomQueries(conn);
        Room test = query.getRoom(res.getInt(1));

        Assert.assertNotNull(test);
        Assert.assertEquals(name,test.getName());
        Assert.assertEquals(location, test.getLocation());
        Assert.assertEquals(capacity, test.getCapacity());
    }
}
