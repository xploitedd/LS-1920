package pt.isel.ls.sql.api;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static pt.isel.ls.utils.ExceptionUtils.passException;

public class SqlHandler {

    private final Connection conn;

    /**
     * Creates a new SQL Handler
     * @param conn connection where this handler will
     *             perform its actions
     */
    public SqlHandler(Connection conn) {
        this.conn = conn;
    }

    /**
     * Execute an SQL query
     * @param query query to be executed
     * @param params parameters of the query
     * @return result of the execution
     */
    public boolean execute(String query, Object... params) {
        return passException(() -> {
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.execute();
        });
    }

    /**
     * Creates a new update to the database
     * @param query update query
     * @return a new Update
     */
    public Update createUpdate(String query) {
        return passException(() -> new Update(conn.prepareStatement(query)));
    }

    /**
     * Creates a new query to the database
     * @param query query
     * @return a new Query
     */
    public Query createQuery(String query) {
        return passException(() -> new Query(conn.prepareStatement(query)));
    }

}
