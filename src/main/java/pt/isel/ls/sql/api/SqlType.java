package pt.isel.ls.sql.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.stream.Stream;

import static pt.isel.ls.utils.ExceptionUtils.passException;

public abstract class SqlType<T extends SqlType<T, V>, V> {

    protected final PreparedStatement stmt;
    protected int stateIdx = 1;

    SqlType(PreparedStatement stmt) {
        this.stmt = stmt;
    }

    @SuppressWarnings("unchecked")
    public final T bind(BindFunction bindFunction) {
        return (T) passException(() -> {
            bindFunction.bind();
            return this;
        });
    }

    public final T bind(Object value) {
        return bind(() -> stmt.setObject(stateIdx++, value));
    }

    public final T bind(int value) {
        return bind(() -> stmt.setInt(stateIdx++, value));
    }

    public final T bind(String value) {
        return bind(() -> stmt.setString(stateIdx++, value));
    }

    public final T bind(Timestamp value) {
        return bind(() -> stmt.setTimestamp(stateIdx++, value));
    }

    public abstract V execute();

    protected static Stream<Field> getClassNonStaticFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()));
    }

    @FunctionalInterface
    private interface BindFunction {
        void bind() throws Exception;
    }

}
