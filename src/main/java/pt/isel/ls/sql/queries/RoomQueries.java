package pt.isel.ls.sql.queries;

import java.sql.Connection;
import pt.isel.ls.model.Model;

public class RoomQueries extends DatabaseQueries {

    public RoomQueries(Connection conn) {
        super(conn);
    }

    public Iterable<Model> createNewRoom(String labelName) throws Throwable {
        return null;
    }

    public Iterable<Model> getRoom() throws Throwable {
        return null;
    }

}
