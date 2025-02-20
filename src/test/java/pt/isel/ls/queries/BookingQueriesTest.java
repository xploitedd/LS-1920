package pt.isel.ls.queries;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.DatasourceUtils;
import pt.isel.ls.model.Booking;
import pt.isel.ls.sql.api.SqlHandler;
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
    private static final LocalDateTime beginLdt = LocalDateTime.now().plusHours(1).withMinute(10);
    private static final Timestamp begin = Timestamp.valueOf(beginLdt);
    private static final Timestamp end = Timestamp.valueOf(beginLdt.plusDays(1));


    @Before
    public void beforeEach() throws Throwable {
        DatasourceUtils.executeFile("CreateTables.sql");
        Connection conn = dSource.getConnection();
        SqlHandler handler = new SqlHandler(conn);
        UserQueries testUser = new UserQueries(handler);
        userID = testUser.createNewUser("TestUser","user@testing.booking").getUid();
        RoomQueries testRoom = new RoomQueries(handler);
        roomID = testRoom.createNewRoom(
                "TestRoom","Tests",42, null, new LinkedList<>()
        ).getRid();
        conn.close();
    }

    @Test
    public void testCreateNewBooking() throws Throwable {
        Connection conn = dSource.getConnection();
        SqlHandler handler = new SqlHandler(conn);
        BookingQueries query = new BookingQueries(handler);

        Booking booking = query.createNewBooking(roomID, userID, begin, end);
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM booking  WHERE bid = ?"
        );

        stmt.setInt(1, booking.getBid());
        ResultSet res = stmt.executeQuery();
        Assert.assertTrue(res.next());
        conn.close();
    }

    @Test
    public void testGetBookingByRidUidBeginAndEnd() throws Throwable {
        Connection conn = dSource.getConnection();
        SqlHandler handler = new SqlHandler(conn);
        BookingQueries query = new BookingQueries(handler);

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO booking (rid, uid, begin, \"end\") VALUES (?, ?, ?, ?);"
        );
        stmt.setInt(1, roomID);
        stmt.setInt(2, userID);
        stmt.setTimestamp(3, begin);
        stmt.setTimestamp(4, end);
        stmt.execute();

        Booking test = query.getBooking(roomID, userID, begin, end);

        Assert.assertNotNull(test);
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

        SqlHandler handler = new SqlHandler(conn);
        BookingQueries query = new BookingQueries(handler);
        Booking test = query.getBooking(res.getInt(1));

        Assert.assertNotNull(test);
        conn.close();
    }
}
