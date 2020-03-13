package pt.isel.ls.router;

import java.util.stream.Stream;

public class RouterUtils {

    static String[] getValidSegments(String route) {
        return Stream.of(route.split("/"))
                .filter(x -> !x.isBlank())
                .toArray(String[]::new);
    }

}
