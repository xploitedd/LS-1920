package pt.isel.ls.sql.api;

import pt.isel.ls.exceptions.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Callable;

public class SqlHandler {

    private final Connection conn;

    public SqlHandler(Connection conn) {
        this.conn = conn;
    }

    public boolean execute(String query, Object... params) {
        return passException(() -> {
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.execute();
        });
    }

    public Update createUpdate(String query) {
        return passException(() -> new Update(conn.prepareStatement(query)));
    }

    public Query createQuery(String query) {
        return passException(() -> new Query(conn.prepareStatement(query)));
    }

    public static <T> T passException(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

}
