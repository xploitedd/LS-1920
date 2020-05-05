package pt.isel.ls.sql;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.api.SqlHandler;

public class ConnectionProvider {

    private final DataSource dataSource;

    public ConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <U> U execute(Provider<U> queries) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                U res = queries.apply(new SqlHandler(conn));
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw new RouteException(e.getMessage());
            }
        } catch (Throwable e) {
            throw new RouteException(e.getMessage());
        }
    }

}
