package pt.isel.ls.sql.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Query extends SqlType<Query, ResultSet> {

    private static final HashMap<Class<?>, Field[]> FIELD_CACHE = new HashMap<>();
    private static final HashMap<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new HashMap<>();

    Query(PreparedStatement stmt) {
        super(stmt);
    }

    @Override
    public ResultSet execute() {
        return SqlHandler.passException(stmt::executeQuery);
    }

    public <T> Stream<T> mapToClass(Class<T> clazz) {
        ResultSet rs = execute();
        Stream<ResultSet> rsStream = StreamSupport.stream(new ResultSetSpliterator(rs), false);
        return rsStream.map(set -> mapToClass(clazz, set));
    }

    @SuppressWarnings("unchecked")
    private static <T> T mapToClass(Class<T> clazz, ResultSet rs) {
        return SqlHandler.passException(() -> {
            Field[] fields = FIELD_CACHE.get(clazz);
            if (fields == null) {
                fields = getClassNonStaticFields(clazz)
                        .toArray(Field[]::new);

                FIELD_CACHE.put(clazz, fields);
            }

            Object[] fieldValues = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                fieldValues[i] = rs.getObject(f.getName());
            }

            Constructor<T> ctor = (Constructor<T>) CONSTRUCTOR_CACHE.get(clazz);
            if (ctor == null) {
                ctor = (Constructor<T>) clazz.getDeclaredConstructors()[0];
                CONSTRUCTOR_CACHE.put(clazz, ctor);
            }

            return ctor.newInstance(fieldValues);
        });
    }

    private static class ResultSetSpliterator extends Spliterators.AbstractSpliterator<ResultSet> {

        private final ResultSet resultSet;

        protected ResultSetSpliterator(ResultSet resultSet) {
            super(Long.MAX_VALUE, 0);
            this.resultSet = resultSet;
        }

        @Override
        public boolean tryAdvance(Consumer<? super ResultSet> action) {
            return SqlHandler.passException(() -> {
                if (resultSet.next()) {
                    action.accept(resultSet);
                    return true;
                }

                return false;
            });
        }

    }

}
