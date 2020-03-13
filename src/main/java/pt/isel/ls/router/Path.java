package pt.isel.ls.router;

import java.util.Optional;

public class Path {

    private static final Path root = new Path(new String[0]);

    private final String[] pathSegments;

    private Path(String[] pathSegments) {
        this.pathSegments = pathSegments;
    }

    public String[] getPathSegments() {
        return pathSegments;
    }

    public static Optional<Path> of(String pathString) {
        if (pathString == null || pathString.isBlank()) {
            return Optional.empty();
        }

        /* check if path is root, and if it is we don't need to create another
         object for it */
        if (pathString.equals("/")) {
            return Optional.of(root);
        }

        // path has to start with "/"
        if (!pathString.startsWith("/")) {
            return Optional.empty();
        }

        // split the pathString into segments and get only the non-blank segments
        String[] segments = RouterUtils.getValidSegments(pathString);
        return Optional.of(new Path(segments));
    }

}
