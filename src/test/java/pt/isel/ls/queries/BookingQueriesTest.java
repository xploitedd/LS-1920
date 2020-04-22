package pt.isel.ls.queries;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.model.Booking;
import pt.isel.ls.sql.queries.BookingQueries;
import pt.isel.ls.sql.queries.RoomQueries;
import pt.isel.ls.sql.queries.UserQueries;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class BookingQueriesTest {

    private static final DataSource dSource = DatasourceUtils.getDataSource();

    private static int userID;
    private static int roomID;
    private final Timestamp begin = Timestamp.valueOf(LocalDateTime.of(2020,2,3,4,10));
    private final Timestamp end = Timestamp.valueOf(LocalDateTime.of(2021,2,3,4,10));

    @Before
    public void beforeEach() throws Throwable {
        DatasourceUtils.executeFile(dSource, "src/test/resources/sql/CreateTables.sql");
        Connection conn = dSource.getConnection();
        UserQueries testUser = new UserQueries(conn);
        userID = testUser.createNewUser("TestUser","user@testing.booking").getUid();
        RoomQueries testRoom = new RoomQueries(conn);
        roomID = testRoom.createNewRoom(
                "TestRoom","Tests",42, null, new LinkedList<>()
        ).getRid();
        conn.close();
    }

    @Test
    public void testCreateNewBooking() throws Throwable {
        Connection conn = dSource.getConnection();
        BookingQueries query = new BookingQueries(conn);

        query.createNewBooking(roomID, userID, begin, end);

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT rid, uid, begin, \"end\" FROM booking"
                        + " WHERE rid = ? AND uid = ? AND begin = ? AND \"end\" = ?"
        );
        stmt.setInt(1, roomID);
        stmt.setInt(2, userID);
        stmt.setTimestamp(3, begin);
        stmt.setTimestamp(4, end);
        ResultSet res = stmt.executeQuery();

        Assert.assertTrue(res.next());
        Assert.assertEquals(roomID, res.getInt(1));
        Assert.assertEquals(userID, res.getInt(2));
        Assert.assertEquals(begin, res.getTimestamp(3));
        Assert.assertEquals(end, res.getTimestamp(4));
        conn.close();
    }

    @Test
    public void testGetBookingByRidUidBeginAndEnd() throws Throwable {
        Connection conn = dSource.getConnection();
        BookingQueries query = new BookingQueries(conn);

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO booking (rid, uid, begin, \"end\") VALUES (?, ?, ?, ?);"
        );
        stmt.setInt(1, roomID);
        stmt.setInt(2, userID);
        stmt.setTimestamp(3, begin);
        stmt.setTimestamp(4, end);
        stmt.execute();

        Booking test = query.getBooking(roomID,userID,begin,end);

        Assert.assertNotNull(test);
        Assert.assertEquals(roomID,test.getRid());
        Assert.assertEquals(userID, test.getUid());
        Assert.assertEquals(begin, test.getBegin());
        Assert.assertEquals(end, test.getEnd());
        conn.close();
    }

    @Test
    public void testGetBookingById() throws Throwable {
        Connection conn = dSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO booking (rid, uid, begin, \"end\") VALUES (?, ?, ?, ?);"
        );
        stmt.setInt(1, roomID);
        stmt.setInt(2, userID);
        stmt.setTimestamp(3, begin);
        stmt.setTimestamp(4, end);
        stmt.execute();

        stmt = conn.prepareStatement(
                "SELECT bid FROM booking"
                        + " WHERE rid = ? AND uid = ? AND begin = ? AND \"end\" = ?"
        );
        stmt.setInt(1, roomID);
        stmt.setInt(2, userID);
        stmt.setTimestamp(3, begin);
        stmt.setTimestamp(4, end);
        ResultSet res = stmt.executeQuery();

        Assert.assertTrue(res.next());

        BookingQueries query = new BookingQueries(conn);
        Booking test = query.getBooking(res.getInt(1));

        Assert.assertNotNull(test);
        Assert.assertEquals(roomID,test.getRid());
        Assert.assertEquals(userID, test.getUid());
        Assert.assertEquals(begin, test.getBegin());
        Assert.assertEquals(end, test.getEnd());
        conn.close();
    }
}
