package pt.isel.ls.sql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import pt.isel.ls.model.Booking;

public class BookingQueries extends DatabaseQueries {

    public BookingQueries(Connection conn) {
        super(conn);
    }

    public Booking createNewBooking(int rid, int uid, Timestamp begin, Timestamp end) throws Throwable {
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO booking (begin,\"end\",rid,uid) VALUES (?,?,?,?);"
        );

        stmt.setTimestamp(1,begin);
        stmt.setTimestamp(2,end);
        stmt.setInt(3,rid);
        stmt.setInt(4,uid);
        stmt.execute();
        return getBooking(rid,uid,begin,end);
    }

    public Booking getBooking(int rid, int uid, Timestamp begin, Timestamp end) throws Throwable {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT bid FROM booking WHERE begin = ? AND end = ? AND rid = ? AND uid = ?;"
        );

        ret.setTimestamp(1,begin);
        ret.setTimestamp(2,end);
        ret.setInt(3,rid);
        ret.setInt(4,uid);
        ResultSet rs = ret.executeQuery();
        rs.next();
        int bid = rs.getInt("bid");
        return new Booking(bid,rid,uid,begin,end);
    }

    public Iterable<Booking> getBookings() throws Throwable {
        PreparedStatement ret = conn.prepareStatement(
                "SELECT * FROM booking"
        );
        ResultSet rs = ret.executeQuery();
        LinkedList<Booking> results = new LinkedList<>();
        while (rs.next()) {
            results.add(new Booking(rs.getInt("bid"),rs.getInt("rid"),
                    rs.getInt("uid"),rs.getTimestamp("begin"),rs.getTimestamp("end")));
        }
        return results;
    }

}
