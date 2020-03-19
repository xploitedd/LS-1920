package pt.isel.ls.sql;

import java.sql.Connection;
import javax.sql.DataSource;

public class ConnectionProvider {

    private final DataSource dataSource;

    public ConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <U> U execute(Provider<U> queries) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            return queries.apply(conn);
        }
    }

}
