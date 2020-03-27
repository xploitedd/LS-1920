package pt.isel.ls.queries;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.TestDatasource;
import pt.isel.ls.model.Label;
import pt.isel.ls.model.Room;
import pt.isel.ls.sql.queries.RoomLabelQueries;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static pt.isel.ls.DatabaseTest.executeFile;

public class RoomLabelQueriesTest {

    private static final DataSource dSource = TestDatasource.getDataSource();


    @Before
    public void resetTables() throws SQLException, IOException {
        executeFile("src/test/sql/CreateTables.sql");
    }

    @Test
    public void testAddRoomLabels() throws Throwable {
        Connection conn = dSource.getConnection();

        String labelName = "TestingLabel";
        Label test = new Label(1, labelName);
        List<Label> list = new LinkedList<>();
        list.add(test);

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, labelName);
        stmt.execute();

        PreparedStatement rstmt = conn.prepareStatement("INSERT INTO room (name, location, capacity)"
                + " VALUES (?, ?, ?);");
        rstmt.setString(1, "test");
        rstmt.setString(2, "Sei la");
        rstmt.setInt(3, 2);
        rstmt.execute();

        RoomLabelQueries query = new RoomLabelQueries(conn);
        query.addRoomLabels(list,1);

        PreparedStatement lstmt = conn.prepareStatement(
                "SELECT l.lid, name FROM "
                        + "(room_label rl JOIN label l on rl.lid = l.lid) WHERE rid = ?"
        );
        lstmt.setInt(1,1);
        ResultSet res = lstmt.executeQuery();

        if (res.next()) {
            Assert.assertEquals(1,res.getInt(1));
        } else {
            Assert.fail("Empty");
        }
    }

    @Test
    public void testGetRoomLabels() throws Throwable {
        Connection conn = dSource.getConnection();

        PreparedStatement rstmt = conn.prepareStatement("INSERT INTO room (name, location, capacity)"
                + " VALUES (?, ?, ?);");
        rstmt.setString(1, "test1");
        rstmt.setString(2, "Sei la");
        rstmt.setInt(3, 2);
        rstmt.execute();

        String labelName = "TestingLabel1";
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, labelName);
        stmt.execute();

        PreparedStatement rl = conn.prepareStatement(
                "INSERT INTO room_label (lid, rid) VALUES (?, ?);"
        );

        rl.setInt(1, 1);
        rl.setInt(2, 1);
        rl.execute();

        RoomLabelQueries query = new RoomLabelQueries(conn);
        Iterable<Label> iter = query.getRoomLabels(1);

        for (Label lbl: iter) {
            Assert.assertEquals(labelName, lbl.getName());
            Assert.assertEquals(1,lbl.getLid());
        }
    }

    @Test
    public void testGetLabeledRooms() throws Throwable {
        Connection conn = dSource.getConnection();

        PreparedStatement rstmt = conn.prepareStatement("INSERT INTO room (name, location, capacity)"
                + " VALUES (?, ?, ?);");
        rstmt.setString(1, "test2");
        rstmt.setString(2, "Sei la");
        rstmt.setInt(3, 2);
        rstmt.execute();

        String labelName = "TestingLabel2";
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, labelName);
        stmt.execute();

        PreparedStatement rl = conn.prepareStatement(
                "INSERT INTO room_label (lid, rid) VALUES (?, ?);"
        );

        rl.setInt(1, 1);
        rl.setInt(2, 1);
        rl.execute();

        RoomLabelQueries query = new RoomLabelQueries(conn);
        Iterable<Room> iter = query.getLabeledRooms(3);

        for (Room room: iter) {
            Assert.assertEquals("test2", room.getName());
            Assert.assertEquals(3,room.getRid());
            Assert.assertEquals("Sei la",room.getLocation());
            Assert.assertEquals(2,room.getCapacity());
        }
    }
}
