package pt.isel.ls.sql;

import java.sql.Connection;

public interface Provider<U> {

    U apply(Connection connection) throws Throwable;

}
