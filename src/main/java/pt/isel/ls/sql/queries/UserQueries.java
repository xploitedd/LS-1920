package pt.isel.ls.sql.queries;

import java.sql.Connection;
import pt.isel.ls.model.Model;

public class UserQueries extends DatabaseQueries {

    public UserQueries(Connection conn) {
        super(conn);
    }

    public Iterable<Model> createNewUser(String labelName) throws Throwable {
        return null;
    }

    public Iterable<Model> getUser() throws Throwable {
        return null;
    }

}
