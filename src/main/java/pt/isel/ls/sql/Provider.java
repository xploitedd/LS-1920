package pt.isel.ls.sql;

import pt.isel.ls.sql.api.SqlHandler;

@FunctionalInterface
public interface Provider<U> {

    U apply(SqlHandler handler) throws Exception;

}
