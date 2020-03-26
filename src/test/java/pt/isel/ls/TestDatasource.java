package pt.isel.ls;

import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class TestDatasource {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_TEST_DATABASE_URL";
    private static final PGSimpleDataSource dataSource = new PGSimpleDataSource();

    public static DataSource getDataSource() {
        dataSource.setUrl(System.getenv(DATABASE_CONNECTION_ENV));
        return dataSource;
    }

}
