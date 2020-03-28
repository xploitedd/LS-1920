package pt.isel.ls.model;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public final class Booking {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final int bid;
    private final int rid;
    private final int uid;
    private final Timestamp begin;
    private final Timestamp end;

    /**
     * Creates a new booking
     * @param bid id of the booking
     * @param rid id of the room that was booked
     * @param uid id of the user who did the booking
     * @param begin begin instant of the reservation
     * @param end end instant of the reservation
     */
    public Booking(int bid, int rid, int uid, Timestamp begin, Timestamp end) {
        this.bid = bid;
        this.rid = rid;
        this.uid = uid;
        this.begin = begin;
        this.end = end;
    }

    /**
     * Get Booking Id
     * @return Booking Id
     */
    public int getBid() {
        return bid;
    }

    /**
     * Get Room Id
     * @return Room Id
     */
    public int getRid() {
        return rid;
    }

    /**
     * Get Begin Instant
     * @return Begin Instant
     */
    public Timestamp getBegin() {
        return begin;
    }

    /**
     * Get End Instant
     * @return End Instant
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * Get User Id
     * @return User Id
     */
    public int getUid() {
        return uid;
    }

    @Override
    public String toString() {
        String beginString = begin.toLocalDateTime().format(formatter);
        String endString = end.toLocalDateTime().format(formatter);
        return "[" + beginString + " - " + endString + "]: "
                + bid + " @ room " + rid + " by user " + uid;
    }

}
