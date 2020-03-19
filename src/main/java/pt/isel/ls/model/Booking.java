package pt.isel.ls.model;

import java.sql.Timestamp;

public class Booking {
    private final int bid;
    private final int rid;
    private final int uid;
    private final Timestamp begin;
    private final Timestamp end;

    public Booking(int bid, int rid, int uid, Timestamp begin, Timestamp end) {
        this.bid = bid;
        this.rid = rid;
        this.uid = uid;
        this.begin = begin;
        this.end = end;
    }

    public int getBid() {
        return bid;
    }

    public int getRid() {
        return rid;
    }

    public Timestamp getBegin() {
        return begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public int getUid() {
        return uid;
    }
}
