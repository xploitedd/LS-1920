package pt.isel.ls.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

public class RouteTemplate {

    private final ArrayList<TemplateSegment> templateSegments;

    /**
     * Creates a new RouteTemplate with template segments
     * @param templateSegments template segments of this template
     */
    private RouteTemplate(ArrayList<TemplateSegment> templateSegments) {
        this.templateSegments = templateSegments;
    }

    /**
     * Matches a path with this template
     * If it's a match then it will return the path parameters,
     * otherwise returns empty
     * @param path path of the request
     * @return path parameters or empty
     */
    public Optional<HashMap<String, String>> match(Path path) {
        HashMap<String, String> pathParameters = new HashMap<>();
        Iterator<TemplateSegment> templateSegmentIterator = templateSegments.iterator();
        for (String segment : path.getPathSegments()) {
            if (templateSegmentIterator.hasNext()) {
                TemplateSegment templateSegment = templateSegmentIterator.next();
                // check if it matches the actual segment
                if (templateSegment.match(segment)) {
                    if (templateSegment instanceof ParameterTemplateSegment) {
                        // add the parameter to the map
                        ParameterTemplateSegment pts = (ParameterTemplateSegment) templateSegment;
                        pathParameters.put(pts.segment, segment);
                    }
                } else {
                    return Optional.empty();
                }
            }
        }

        // check if there are any obligatory parameters ahead
        if (templateSegmentIterator.hasNext()) {
            TemplateSegment templateSegment = templateSegmentIterator.next();
            if (templateSegment instanceof ParameterTemplateSegment) {
                ParameterTemplateSegment pts = (ParameterTemplateSegment) templateSegment;
                // only the last segment can be optional according to the contract
                if (pts.isObligatory) {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(pathParameters);
    }

    /**
     * Parses a template string into a RouteTemplate
     * @param routeString template string to be parsed
     * @return a new instance of RouteTemplate
     */
    public static RouteTemplate of(String routeString) {
        ArrayList<TemplateSegment> templateSegments = new ArrayList<>();
        String[] segments = RouterUtils.getValidSegments(routeString);
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            if (isParameterSegment(segment)) {
                // guarantee that only the last segment can be optional
                boolean isObligatory = isObligatoryParameter(segment) || i + 1 != segments.length;
                templateSegments.add(new ParameterTemplateSegment(
                        getParameterName(segment),
                        isObligatory
                ));
            } else {
                templateSegments.add(new ConstantTemplateSegment(segment));
            }
        }

        return new RouteTemplate(templateSegments);
    }

    /**
     * Gets a segment parameter name
     * @param segment parameter segment
     * @return parameter name
     */
    private static String getParameterName(String segment) {
        return segment.substring(1, segment.indexOf('}'));
    }

    /**
     * Checks if a segment is a parameter
     * @param segment segment to be checked
     * @return true if a parameter, false otherwise
     */
    private static boolean isParameterSegment(String segment) {
        return segment.startsWith("{") && segment.endsWith("}") && segment.length() > 2;
    }

    /**
     * Checks if a segment is a obligatory parameter
     * @param segment segment to be checked
     * @return true if an obligatory parameter, false otherwise
     */
    private static boolean isObligatoryParameter(String segment) {
        return !segment.endsWith("}?");
    }

    private abstract static class TemplateSegment {

        protected String segment;

        protected TemplateSegment(String segment) {
            this.segment = segment;
        }

        public abstract boolean match(String segment);

    }

    private static class ConstantTemplateSegment extends TemplateSegment {

        protected ConstantTemplateSegment(String segment) {
            super(segment);
        }

        @Override
        public boolean match(String segment) {
            return segment.equals(this.segment);
        }

    }

    private static class ParameterTemplateSegment extends TemplateSegment {

        private boolean isObligatory;

        protected ParameterTemplateSegment(String segment, boolean isObligatory) {
            super(segment);
            this.isObligatory = isObligatory;
        }

        @Override
        public boolean match(String segment) {
            return true;
        }

    }

}
