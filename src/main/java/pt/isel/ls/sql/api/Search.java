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

public class Search extends SqlType<Search, ResultSet> {

    private static final ConcurrentHashMap<Class<?>, Field[]> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();

    Search(PreparedStatement stmt) {
        super(stmt);
    }

    @Override
    public ResultSet execute() {
        return passException(stmt::executeQuery);
    }

    /**
     * Map the ResultSet of this query to a data class
     * @param clazz data class that represents this result set
     * @param <T> type of the data class
     * @return a new Stream with the data
     */
    public <T> Stream<T> mapToClass(Class<T> clazz) {
        ResultSet rs = execute();
        Stream<ResultSet> rsStream = StreamSupport.stream(new ResultSetSpliterator(rs), false);

        Field[] fields = getFields(clazz);
        Constructor<T> ctor = getConstructor(clazz);

        return rsStream.map(set -> mapToClass(fields, ctor, set));
    }

    /**
     * Map the ResultSet of this query to a data class
     * @param fields field objects of the data class
     * @param ctor constructor of the data class
     * @param rs result set to be mapped
     * @param <T> type of the data class
     * @return a class with a row of the result set
     */
    private static <T> T mapToClass(Field[] fields, Constructor<T> ctor, ResultSet rs) {
        return passException(() -> {
            // get the necessary values from the result set
            // all non-static fields must be included in the result set
            Object[] fieldValues = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                fieldValues[i] = rs.getObject(f.getName());
            }

            // get the first constructor of this class
            // ... and create a new instance (fields must be in the same order)
            return ctor.newInstance(fieldValues);
        });
    }

    /**
     * Get non-static fields from a data class
     * @param clazz data class
     * @return field array
     */
    private static synchronized Field[] getFields(Class<?> clazz) {
        // use cache to optimize field retrieval
        Field[] fields = FIELD_CACHE.get(clazz);
        if (fields == null) {
            // we only want non-static fields
            fields = getClassNonStaticFields(clazz)
                    .toArray(Field[]::new);

            FIELD_CACHE.put(clazz, fields);
        }

        return fields;
    }

    /**
     * Get data constructor from the specified data class
     * @param clazz data class
     * @param <T> type of the class
     * @return constructor
     */
    @SuppressWarnings("unchecked")
    private static synchronized <T> Constructor<T> getConstructor(Class<T> clazz) {
        // the data constructor must be the only constructor in the class
        // cache is also used to optimize constructor retrieval
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
