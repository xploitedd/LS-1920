package pt.isel.ls.sql.queries;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Stream;

import pt.isel.ls.model.Booking;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.api.SqlHandler;
import pt.isel.ls.utils.Interval;

public class BookingQueries extends DatabaseQueries {

    private static final int BOOKING_MIN_TIME = 10;
    private static final int BOOKING_MINUTE_MUL = 10;

    public BookingQueries(SqlHandler handler) {
        super(handler);
    }

    /**
     * Creates a new Booking
     * @param rid id of the room for the booking
     * @param uid user id of the booking owner
     * @param begin begin instant of the booking
     * @param end end instant of the booking
     * @return the booking that was created
     */
    public Booking createNewBooking(int rid, int uid, Timestamp begin, Timestamp end) {
        doBookingConstraintCheck(begin, end);
        // check overlapping bookings
        Interval i2 = new Interval(begin.getTime(), end.getTime());
        getBookingsByRid(rid).forEach(booking -> checkOverlap(booking, i2));

        handler.createUpdate("INSERT INTO booking (begin, \"end\", rid, uid) VALUES (?, ?, ?, ?);")
                .bind(begin)
                .bind(end)
                .bind(rid)
                .bind(uid)
                .execute();

        return getBooking(rid, uid, begin, end);
    }

    /**
     * Get Booking by parameters
     * @param rid room id of the booking
     * @param uid user id of the booking
     * @param begin begin timestamp
     * @param end end timestamp
     * @return a Booking
     */
    public Booking getBooking(int rid, int uid, Timestamp begin, Timestamp end) {
        Optional<Booking> booking = handler
                .createQuery("SELECT * FROM booking WHERE begin = ? AND \"end\" = ? AND rid = ? AND uid = ?;")
                .bind(begin)
                .bind(end)
                .bind(rid)
                .bind(uid)
                .mapToClass(Booking.class)
                .findFirst();

        if (booking.isEmpty()) {
            throw new RouteException("A booking was not found!");
        }

        return booking.get();
    }

    /**
     * Get booking by id
     * @param bid id of the booking
     * @return a Booking
     */
    public Booking getBooking(int bid) {
        Optional<Booking> booking = handler
                .createQuery("SELECT * FROM booking WHERE bid = ?;")
                .bind(bid)
                .mapToClass(Booking.class)
                .findFirst();

        if (booking.isEmpty()) {
            throw new RouteException("A booking with id " + bid + " was not found!");
        }

        return booking.get();
    }

    /**
     * Retrieve all Bookings
     * @return all Bookings
     */
    public Stream<Booking> getBookings() {
        return handler.createQuery("SELECT * FROM booking")
                .mapToClass(Booking.class);
    }

    /**
     * Get all bookings that have the specified owner
     * @param uid user id of the owner
     * @return bookings owned by uid
     */
    public Stream<Booking> getBookingsByUid(int uid) {
        return handler.createQuery("SELECT * FROM booking WHERE uid = ?")
                .bind(uid)
                .mapToClass(Booking.class);
    }

    /**
     * Get all bookings occurring in a room
     * @param rid room where the bookings are occurring
     * @return bookings with room rid
     */
    public Stream<Booking> getBookingsByRid(int rid) {
        return handler.createQuery("SELECT * FROM booking WHERE rid = ?")
                .bind(rid)
                .mapToClass(Booking.class);
    }

    public Booking editBooking(int rid, int bid, int newUid, Timestamp newBegin, Timestamp newEnd) throws Exception {
        doBookingConstraintCheck(newBegin, newEnd);
        Interval newInt = new Interval(newBegin.getTime(), newEnd.getTime());
        getBookingsByRid(rid)
                .filter(booking -> booking.getBid() != bid)
                .forEach(booking -> checkOverlap(booking, newInt));

        int res = handler
                .createUpdate("UPDATE booking SET uid = ?, begin = ?, \"end\" = ? WHERE bid = ?")
                .bind(newUid)
                .bind(newBegin)
                .bind(newEnd)
                .bind(bid)
                .execute();

        if (res > 0) {
            return new Booking(bid, rid, newUid, newBegin, newEnd);
        }

        throw new RouteException("Couldn't update the booking!");
    }

    public int deleteBooking(int rid, int bid) {
        return handler.createUpdate("DELETE FROM booking WHERE rid = ? AND bid = ?")
            .bind(rid)
            .bind(bid)
            .execute();
    }

    private static void checkOverlap(Booking b1, Interval i2) throws RouteException {
        Interval i1 = new Interval(b1.getBegin().getTime(), b1.getEnd().getTime());
        if (i1.isOverlapping(i2)) {
            throw new RouteException("This booking overlaps with another booking!");
        }
    }

    private static void doBookingConstraintCheck(Timestamp begin, Timestamp end) throws RouteException {
        long lbegin = begin.getTime() / 1000;
        long lend = end.getTime() / 1000;
        if (lend - lbegin < BOOKING_MIN_TIME) {
            throw new RouteException("A booking should last for at least " + BOOKING_MIN_TIME);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(begin.getTime());
        long min1 = cal.get(Calendar.MINUTE);
        cal.setTimeInMillis(end.getTime());
        long min2 = cal.get(Calendar.MINUTE);

        if (min1 % BOOKING_MINUTE_MUL != 0 || min2 % BOOKING_MINUTE_MUL != 0) {
            throw new RouteException("Minutes should be multiples of " + BOOKING_MINUTE_MUL);
        }
    }

}
