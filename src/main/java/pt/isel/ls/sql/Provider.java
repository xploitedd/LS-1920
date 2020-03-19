package pt.isel.ls.sql;

import java.sql.Connection;
import pt.isel.ls.model.Model;

public interface Provider {

    Iterable<Model> apply(Connection connection) throws Throwable;

}
