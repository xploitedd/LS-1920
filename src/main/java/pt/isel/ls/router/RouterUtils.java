package pt.isel.ls.router;

import java.util.stream.Stream;

public class RouterUtils {

    /**
     * Gets all valid segments in a path string
     * Valid segments are separated by an '/' and
     * are not blank
     * @param route path string
     * @return path segments
     */
    static String[] getValidSegments(String route) {
        return Stream.of(route.split("/"))
                .filter(x -> !x.isBlank())
                .toArray(String[]::new);
    }

}
