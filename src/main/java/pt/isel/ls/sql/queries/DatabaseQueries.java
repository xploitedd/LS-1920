package pt.isel.ls.sql.queries;

import java.sql.Connection;

public abstract class DatabaseQueries {

    protected final Connection conn;

    public DatabaseQueries(Connection conn) {
        this.conn = conn;
    }

}
