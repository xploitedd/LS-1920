package pt.isel.ls.sql.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.stream.Stream;

import static pt.isel.ls.utils.ExceptionUtils.passException;

/**
 * A SQL Type handles sql execution for specific types of queries
 *      - an update query (insert, update)
 *      - a data retrieval query (select)
 *      - ...
 * @param <T> Must be a child type of this type
 *           It's used to create a fluent api
 * @param <V> Type of the data return, usually a ResultSet
 */
public abstract class SqlType<T extends SqlType<T, V>, V> {

    protected final PreparedStatement stmt;
    protected int stateIdx = 1;

    /**
     * Create a new SQL Type
     * @param stmt statement to process
     */
    SqlType(PreparedStatement stmt) {
        this.stmt = stmt;
    }

    /**
     * Binds a value to a prepared statement
     * @param bindFunction function that binds the value
     * @return a class that extends of SqlType
     */
    @SuppressWarnings("unchecked")
    public final T bind(BindFunction bindFunction) {
        return (T) passException(() -> {
            bindFunction.bind(stmt);
            return this;
        });
    }

    /**
     * Bind an object to the statement
     * @param value object to bind
     * @return a class that extends of SqlType
     */
    public final T bind(Object value) {
        return bind(stmt -> stmt.setObject(stateIdx++, value));
    }

    /**
     * Bind an int to the statement
     * @param value int to bind
     * @return a class that extends of SqlType
     */
    public final T bind(int value) {
        return bind(stmt -> stmt.setInt(stateIdx++, value));
    }

    /**
     * Bind a string to the statement
     * @param value string to bind
     * @return a class that extends of SqlType
     */
    public final T bind(String value) {
        return bind(stmt -> stmt.setString(stateIdx++, value));
    }

    /**
     * Bind a timestamp to the statement
     * @param value timestamp to bind
     * @return a class that extends of SqlType
     */
    public final T bind(Timestamp value) {
        return bind(stmt -> stmt.setTimestamp(stateIdx++, value));
    }

    /**
     * Execute the Prepared Statement
     * @return the result
     */
    public abstract V execute();

    /**
     * Get non-static fields of a class
     * @param clazz class to get the fields from
     * @return stream of the non-static fields
     */
    protected static Stream<Field> getClassNonStaticFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()));
    }

    @FunctionalInterface
    private interface BindFunction {
        void bind(PreparedStatement stmt) throws Exception;
    }

}
