package pt.isel.ls.db;

import org.postgresql.ds.PGSimpleDataSource;

public class Database extends PGSimpleDataSource {

    private static final String CONNECTION_ENV = "JDBC_DATABASE_URL";

    public Database() {
        setUrl(System.getenv(CONNECTION_ENV));
    }

}
