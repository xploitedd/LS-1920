package pt.isel.ls.db;

import org.postgresql.ds.PGSimpleDataSource;

public final class Database extends PGSimpleDataSource {

    private static final String CONNECTION_ENV = "JDBC_DATABASE_URL";

    private Database() {
        setUrl(System.getenv(CONNECTION_ENV));
    }

    public static Database getInstance() {
        return new Database();
    }

}
