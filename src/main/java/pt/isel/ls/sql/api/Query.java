package pt.isel.ls.sql.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static pt.isel.ls.utils.ExceptionUtils.passException;

public class Query extends SqlType<Query, ResultSet> {

    private static final ConcurrentHashMap<Class<?>, Field[]> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();

    Query(PreparedStatement stmt) {
        super(stmt);
    }

    @Override
    public ResultSet execute() {
        return passException(stmt::executeQuery);
    }

    public <T> Stream<T> mapToClass(Class<T> clazz) {
        ResultSet rs = execute();
        Stream<ResultSet> rsStream = StreamSupport.stream(new ResultSetSpliterator(rs), false);
        return rsStream.map(set -> mapToClass(clazz, set));
    }

    private static <T> T mapToClass(Class<T> clazz, ResultSet rs) {
        return passException(() -> {
            Field[] fields = getFields(clazz);
            Object[] fieldValues = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                fieldValues[i] = rs.getObject(f.getName());
            }

            Constructor<T> ctor = getConstructor(clazz);
            return ctor.newInstance(fieldValues);
        });
    }

    private static synchronized Field[] getFields(Class<?> clazz) {
        Field[] fields = FIELD_CACHE.get(clazz);
        if (fields == null) {
            fields = getClassNonStaticFields(clazz)
                    .toArray(Field[]::new);

            FIELD_CACHE.put(clazz, fields);
        }

        return fields;
    }

    @SuppressWarnings("unchecked")
    private static synchronized <T> Constructor<T> getConstructor(Class<T> clazz) {
        return (Constructor<T>) CONSTRUCTOR_CACHE.computeIfAbsent(clazz, k ->
                clazz.getDeclaredConstructors()[0]);
    }

    private static class ResultSetSpliterator extends Spliterators.AbstractSpliterator<ResultSet> {

        private final ResultSet resultSet;

        protected ResultSetSpliterator(ResultSet resultSet) {
            super(Long.MAX_VALUE, 0);
            this.resultSet = resultSet;
        }

        @Override
        public boolean tryAdvance(Consumer<? super ResultSet> action) {
            return passException(() -> {
                if (resultSet.next()) {
                    action.accept(resultSet);
                    return true;
                }

                return false;
            });
        }

    }

}
