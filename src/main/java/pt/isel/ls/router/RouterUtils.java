package pt.isel.ls.router;

import pt.isel.ls.router.response.RouteException;

import java.util.stream.Stream;

public class RouterUtils {

    /**
     * Gets all valid segments in a path string
     * Valid segments are separated by an '/' and
     * are not blank
     * @param route path string
     * @return path segments
     */
    public static String[] getValidSegments(String route) {
        return Stream.of(route.split("/"))
                .filter(x -> !x.isBlank())
                .toArray(String[]::new);
    }

    public static void forEachKeyValue(
            String sections,
            String sectionSeparator,
            String kvSeparator,
            KeyValueFunction function
    ) throws RouteException {
        String[] kvSections = sections.split(sectionSeparator);
        for (String s : kvSections) {
            String[] kv = s.split(kvSeparator);
            if (kv.length != 2) {
                throw new RouteException("Key-Value sections must contain a key and value");
            }

            String key = kv[0];
            String value = kv[1];
            if (key.isBlank() || value.isBlank()) {
                throw new RouteException("Key and/or Value are/is blank!");
            }

            function.apply(key, value);
        }
    }

    @FunctionalInterface
    public interface KeyValueFunction {
        void apply(String key, String value);
    }

}
