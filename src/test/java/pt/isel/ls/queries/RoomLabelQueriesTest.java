package pt.isel.ls.queries;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
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

public class RoomLabelQueriesTest {

    private static final DataSource dSource = DatasourceUtils.getDataSource();

    private static final String rName = "testRoom";
    private static final String rLocation = "testLocation";
    private static final int rCapacity = 2;
    private static final int rid = 1;

    private static final String lName = "testLabel";
    private static final int lid = 1;

    @Before
    public void resetTables() throws SQLException, IOException {
        DatasourceUtils.executeFile(dSource,"src/test/resources/sql/CreateTables.sql");
    }

    @Test
    public void testAddRoomLabels() throws Throwable {
        Connection conn = dSource.getConnection();

        Label test = new Label(1, lName);
        List<Label> list = new LinkedList<>();
        list.add(test);

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, lName);
        stmt.execute();

        PreparedStatement rstmt = conn.prepareStatement("INSERT INTO room (name, location, capacity)"
                + " VALUES (?, ?, ?);");
        rstmt.setString(1, rName);
        rstmt.setString(2, rLocation);
        rstmt.setInt(3, rCapacity);
        rstmt.execute();

        RoomLabelQueries query = new RoomLabelQueries(conn);
        query.addRoomLabels(list,rid);

        PreparedStatement lstmt = conn.prepareStatement(
                "SELECT l.lid, name FROM "
                        + "(room_label rl JOIN label l on rl.lid = l.lid) WHERE rid = ?"
        );
        lstmt.setInt(1,rid);
        ResultSet res = lstmt.executeQuery();

        Assert.assertTrue(res.next());
        Assert.assertEquals(lid,res.getInt(1));
        conn.close();
    }

    @Test
    public void testGetRoomLabels() throws Throwable {
        Connection conn = dSource.getConnection();

        PreparedStatement rstmt = conn.prepareStatement("INSERT INTO room (name, location, capacity)"
                + " VALUES (?, ?, ?);");
        rstmt.setString(1, rName);
        rstmt.setString(2, rLocation);
        rstmt.setInt(3, rCapacity);
        rstmt.execute();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, lName);
        stmt.execute();

        PreparedStatement rl = conn.prepareStatement(
                "INSERT INTO room_label (lid, rid) VALUES (?, ?);"
        );

        rl.setInt(1, lid);
        rl.setInt(2, rid);
        rl.execute();

        RoomLabelQueries query = new RoomLabelQueries(conn);
        Iterable<Label> iter = query.getRoomLabels(rid);

        for (Label lbl: iter) {
            Assert.assertEquals(lName, lbl.getName());
            Assert.assertEquals(lid,lbl.getLid());
        }

        conn.close();
    }

    @Test
    public void testGetLabeledRooms() throws Throwable {
        Connection conn = dSource.getConnection();

        PreparedStatement rstmt = conn.prepareStatement("INSERT INTO room (name, location, capacity)"
                + " VALUES (?, ?, ?);");
        rstmt.setString(1, rName);
        rstmt.setString(2, rLocation);
        rstmt.setInt(3, rCapacity);
        rstmt.execute();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO label (name) VALUES (?);");
        stmt.setString(1, lName);
        stmt.execute();

        PreparedStatement rl = conn.prepareStatement(
                "INSERT INTO room_label (lid, rid) VALUES (?, ?);"
        );

        rl.setInt(1, lid);
        rl.setInt(2, rid);
        rl.execute();

        RoomLabelQueries query = new RoomLabelQueries(conn);
        Iterable<Room> iter = query.getLabeledRooms(lid);

        for (Room room: iter) {
            Assert.assertEquals(rName, room.getName());
            Assert.assertEquals(rid,room.getRid());
            Assert.assertEquals(rLocation,room.getLocation());
            Assert.assertEquals(rCapacity,room.getCapacity());
        }

        conn.close();
    }
}
