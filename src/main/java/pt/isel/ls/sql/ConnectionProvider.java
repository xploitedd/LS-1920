package pt.isel.ls.sql;

import java.sql.Connection;
import javax.sql.DataSource;
import pt.isel.ls.model.Model;

public class ConnectionProvider {

    private final DataSource dataSource;

    public ConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Iterable<Model> execute(Provider queries) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            return queries.apply(conn);
        }
    }

}
