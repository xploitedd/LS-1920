package pt.isel.ls.view.utils.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class TableBuilder<T, V, R, U> {

    protected final List<V> header;
    protected final List<List<R>> rows;
    protected final Iterable<T> content;

    public TableBuilder(Iterable<T> content) {
        this.header = new ArrayList<>();
        this.rows = new LinkedList<>();
        this.content = content;
    }

    public TableBuilder<T, V, R, U> withColumn(V columnName, Function<T, R> mapper) {
        Stream<T> content = StreamSupport.stream(this.content.spliterator(), false);
        Iterator<R> iter = content.map(mapper)
                .iterator();

        addColumnData(iter);
        header.add(columnName);
        return this;
    }

    protected void addColumnData(Iterator<R> columnData) {
        for (int i = 0; columnData.hasNext(); i++) {
            R data = columnData.next();
            if (i == rows.size()) {
                rows.add(new ArrayList<>());
            }

            List<R> columns = rows.get(i);
            columns.add(data);
        }
    }

    public abstract U build();

}
