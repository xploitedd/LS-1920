package pt.isel.ls.sql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import pt.isel.ls.model.Booking;
import pt.isel.ls.router.response.RouteException;

public class BookingQueries extends DatabaseQueries {

    public BookingQueries(Connection conn) {
        super(conn);
    }

    /**
     * Creates a new Booking
     * @param rid id of the room for the booking
     * @param uid user id of the booking owner
     * @param begin begin instant of the booking
     * @param end end instant of the booking
     * @return the booking that was created
     * @throws Throwable any exception that occurs
     */
    public Booking createNewBooking(int rid, int uid, Timestamp begin, Timestamp end) throws Throwable {
        // check overlapping bookings
        Iterable<Booking> roomBookings = getBookingsByRid(rid);
        for (Booking b : roomBookings) {
            // s1 -> b.getBegin(), s2 -> begin, e1 -> b.getEnd()
            // s2 >= s1 && s2 < e1
            if (begin.getTime() >= b.getBegin().getTime() && end.getTime() < b.getEnd().getTime()) {
                throw new RouteException("The new booking overlaps another booking!");
            }
        }

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO booking (begin, \"end\", rid, uid) VALUES (?, ?, ?, ?);"
        );

        stmt.setTimestamp(1, begin);
        stmt.setTimestamp(2, end);
        stmt.setInt(3, rid);
        stmt.setInt(4, uid);
        stmt.execute();

        return getBooking(rid, uid, begin, end);
    }

    /**
     * Get Booking by parameters
     * @param rid room id of the booking
     * @param uid user id of the booking
     * @param begin begin timestamp
     * @param end end timestamp
     * @return a Booking
     * @throws Throwable any exception that occurs
     */
    public Booking getBooking(int rid, int uid, Timestamp begin, Timestamp end) throws Throwable {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT bid FROM booking WHERE begin = ? AND \"end\" = ? AND rid = ? AND uid = ?;"
        );

        ret.setTimestamp(1, begin);
        ret.setTimestamp(2, end);
        ret.setInt(3, rid);
        ret.setInt(4, uid);

        ResultSet rs = ret.executeQuery();
        rs.next();

        return new Booking(rs.getInt("bid"), rid, uid, begin, end);
    }

    /**
     * Get booking by id
     * @param bid id of the booking
     * @return a Booking
     * @throws Throwable any exception that occurs
     */
    public Booking getBooking(int bid) throws Throwable {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT begin, \"end\", rid, uid FROM booking WHERE bid = ?;"
        );

        ret.setInt(1, bid);

        ResultSet rs = ret.executeQuery();
        rs.next();

        return new Booking(bid, rs.getInt("rid"), rs.getInt("uid"),
                rs.getTimestamp("begin"), rs.getTimestamp("end"));
    }

    /**
     * Retrieve all Bookings
     * @return all Bookings
     * @throws Throwable any exception that occurs
     */
    public Iterable<Booking> getBookings() throws Throwable {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT * FROM booking"
        );

        ResultSet rs = ret.executeQuery();
        LinkedList<Booking> results = new LinkedList<>();
        while (rs.next()) {
            results.add(new Booking(rs.getInt("bid"), rs.getInt("rid"),
                    rs.getInt("uid"), rs.getTimestamp("begin"),
                    rs.getTimestamp("end")));
        }

        return results;
    }

    /**
     * Get all bookings that have the specified owner
     * @param uid user id of the owner
     * @return bookings owned by uid
     * @throws Throwable any exception that occurs
     */
    public Iterable<Booking> getBookingsByUid(int uid) throws Throwable {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT * FROM booking WHERE uid = ?"
        );

        ret.setInt(1, uid);

        ResultSet rs = ret.executeQuery();
        LinkedList<Booking> results = new LinkedList<>();
        while (rs.next()) {
            results.add(new Booking(rs.getInt("bid"), rs.getInt("rid"),
                    uid, rs.getTimestamp("begin"), rs.getTimestamp("end")));
        }

        return results;
    }

    /**
     * Get all bookings occurring in a room
     * @param rid room where the bookings are occurring
     * @return bookings with room rid
     * @throws Throwable any exception that occurs
     */
    public Iterable<Booking> getBookingsByRid(int rid) throws Throwable {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT * FROM booking WHERE rid = ?"
        );

        ret.setInt(1, rid);

        ResultSet rs = ret.executeQuery();
        LinkedList<Booking> results = new LinkedList<>();
        while (rs.next()) {
            results.add(new Booking(rs.getInt("bid"), rid,
                    rs.getInt("uid"), rs.getTimestamp("begin"),
                    rs.getTimestamp("end")));
        }

        return results;
    }

}
