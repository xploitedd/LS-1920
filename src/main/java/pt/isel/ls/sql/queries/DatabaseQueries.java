package pt.isel.ls.sql.queries;

import pt.isel.ls.sql.api.SqlHandler;

public abstract class DatabaseQueries {

    protected final SqlHandler handler;

    public DatabaseQueries(SqlHandler handler) {
        this.handler = handler;
    }

}
