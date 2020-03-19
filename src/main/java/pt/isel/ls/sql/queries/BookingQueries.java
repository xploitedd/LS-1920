package pt.isel.ls.sql.queries;

import java.sql.Connection;
import pt.isel.ls.model.Model;

public class BookingQueries extends DatabaseQueries {

    public BookingQueries(Connection conn) {
        super(conn);
    }

    public Iterable<Model> createNewBooking(String labelName) throws Throwable {
        return null;
    }

    public Iterable<Model> getBooking() throws Throwable {
        return null;
    }

}
