package pt.isel.ls.sql;

import java.sql.Connection;

@FunctionalInterface
public interface Provider<U> {

    U apply(Connection connection) throws Throwable;

}
