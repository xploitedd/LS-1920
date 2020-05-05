package pt.isel.ls.sql.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class SqlType<T extends SqlType<T, V>, V> {

    protected final PreparedStatement stmt;

    SqlType(PreparedStatement stmt) {
        this.stmt = stmt;
    }

    @SuppressWarnings("unchecked")
    public final T bind(int idx, Object value) {
        return (T) SqlHandler.passException(() -> {
            stmt.setObject(idx + 1, value);
            return this;
        });
    }

    public abstract V execute();

    protected static Stream<Field> getClassNonStaticFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()));
    }

}
