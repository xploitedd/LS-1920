package pt.isel.ls.sql;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import pt.isel.ls.exceptions.router.RouteException;
import pt.isel.ls.sql.api.SqlHandler;

public class ConnectionProvider {

    private final DataSource dataSource;

    /**
     * Creates a new ConnectionProvider
     * @param dataSource data source of the application
     */
    public ConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Provides a new handler to execute transactional
     * queries.
     *
     * This handles is configured with a connection from
     * the data source that closes at the end
     * @param queries Queries to be executed
     * @param <U> Type of the return
     * @return return of the queries
     */
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
