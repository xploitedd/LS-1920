package pt.isel.ls.sql;

import java.sql.Connection;
import javax.sql.DataSource;
import pt.isel.ls.router.RouteException;

public class ConnectionProvider {

    private final DataSource dataSource;

    public ConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <U> U execute(Provider<U> queries) throws RouteException {
        try (Connection conn = dataSource.getConnection()) {
            return queries.apply(conn);
        } catch (Throwable e) {
            throw new RouteException(e.getMessage());
        }
    }

}
