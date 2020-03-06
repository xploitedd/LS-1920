package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class App {

    private static final String DATABASE_CONNECTION_ENV = "JDBC_DATABASE_URL";

    public static void main(String[] args) {
        DataSource dataSource = getDataSource(System.getenv(DATABASE_CONNECTION_ENV));
        System.out.println("Hello LS");
    }

    static DataSource getDataSource(String connectionUrl) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(connectionUrl);
        return dataSource;
    }

}
