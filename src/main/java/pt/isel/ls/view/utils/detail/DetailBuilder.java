package pt.isel.ls.view.utils.detail;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class DetailBuilder<K, V, R> {

    protected final HashMap<K, V> details = new LinkedHashMap<>();

    public DetailBuilder<K, V, R> withDetail(K key, V value) {
        details.put(key, value);
        return this;
    }

    public abstract R build();

}
