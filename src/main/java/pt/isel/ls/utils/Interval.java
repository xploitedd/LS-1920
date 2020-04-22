package pt.isel.ls.utils;

public class Interval {

    private final long begin;
    private final long end;

    public Interval(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    public boolean isOverlapping(Interval other) {
        return !(end <= other.begin || begin >= other.end);
    }

}
