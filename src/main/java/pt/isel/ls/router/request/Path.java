package pt.isel.ls.router.request;

import pt.isel.ls.router.RouterUtils;

import java.util.Optional;

public class Path {

    private static final Path ROOT = new Path(new String[0]);

    private final String[] pathSegments;

    /**
     * Creates a new Path with the specified segments
     * @param pathSegments segments of this path
     */
    private Path(String[] pathSegments) {
        this.pathSegments = pathSegments;
    }

    /**
     * Gets the path segments
     * @return path segmentss
     */
    public String[] getPathSegments() {
        return pathSegments;
    }

    /**
     * Parses a path string into a new instance of Path
     * @param pathString path string to be parsed
     * @return a new Path instance
     */
    public static Optional<Path> of(String pathString) {
        if (pathString == null || pathString.isBlank()) {
            return Optional.empty();
        }

        pathString = pathString.trim();
        /* check if path is root, and if it is we don't need to create another
         object for it */
        if (pathString.equals("/")) {
            return Optional.of(ROOT);
        }

        // path has to start with "/"
        if (!pathString.startsWith("/")) {
            return Optional.empty();
        }

        // split the pathString into segments and get only the non-blank segments
        String[] segments = RouterUtils.getValidSegments(pathString);
        return Optional.of(new Path(segments));
    }

    @Override
    public String toString() {
        return '/' + String.join("/", pathSegments);
    }

}
