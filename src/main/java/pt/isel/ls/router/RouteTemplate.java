package pt.isel.ls.router;

import pt.isel.ls.exceptions.AppException;
import pt.isel.ls.exceptions.parameter.NotEnoughParametersException;
import pt.isel.ls.router.request.Path;
import pt.isel.ls.router.request.parameter.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

public class RouteTemplate {

    private final ArrayList<TemplateSegment> templateSegments;
    private final String stringRepresentation;
    private final int parameterSegmentCount;

    /**
     * Creates a new RouteTemplate with template segments
     * @param templateSegments template segments of this template
     */
    private RouteTemplate(ArrayList<TemplateSegment> templateSegments, int parameterSegmentCount) {
        this.templateSegments = templateSegments;
        this.stringRepresentation = "/" + templateSegments.stream()
                .map(TemplateSegment::toString)
                .reduce((a, b) -> a + "/" + b)
                .orElse("");

        this.parameterSegmentCount = parameterSegmentCount;
    }

    /**
     * Matches a path with this template
     * If it's a match then it will return the path parameters,
     * otherwise returns empty
     * @param path path of the request
     * @return path parameters or empty
     */
    public Optional<HashMap<String, Parameter>> match(Path path) {
        HashMap<String, Parameter> pathParameters = new HashMap<>();
        Iterator<TemplateSegment> templateSegmentIterator = templateSegments.iterator();
        for (String segment : path.getPathSegments()) {
            if (templateSegmentIterator.hasNext()) {
                TemplateSegment templateSegment = templateSegmentIterator.next();
                // check if it matches the actual segment
                if (templateSegment.match(segment)) {
                    if (templateSegment instanceof ParameterTemplateSegment) {
                        // add the parameter to the map
                        ParameterTemplateSegment pts = (ParameterTemplateSegment) templateSegment;
                        pathParameters.put(pts.segment, new Parameter(segment));
                    }
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }

        if (templateSegmentIterator.hasNext()) {
            return Optional.empty();
        }

        return Optional.of(pathParameters);
    }

    /**
     * Construct this path with the specified path parameters
     * @param params path parameters
     * @return path with parameters in place
     */
    public Path constructPathFromTemplate(Object... params) {
        int requiredParams = getParameterSegmentCount();
        if (requiredParams != params.length) {
            throw new NotEnoughParametersException(requiredParams, params.length);
        }

        StringBuilder sb = new StringBuilder();
        int paramIdx = 0;
        for (TemplateSegment ts : templateSegments) {
            sb.append("/");
            if (ts instanceof ParameterTemplateSegment) {
                sb.append(params[paramIdx++]);
            } else {
                sb.append(ts.segment);
            }
        }

        String ps = sb.toString();
        if (ps.equals("")) {
            ps = "/";
        }

        Optional<Path> path = Path.of(ps);
        if (path.isEmpty()) {
            // it's not supposed to launch this exception unless we don't
            // follow the established path contract
            throw new AppException("Unexpected error constructing path from segment");
        }

        return path.get();
    }

    public int getParameterSegmentCount() {
        return parameterSegmentCount;
    }

    /**
     * Parses a template string into a RouteTemplate
     * @param routeString template string to be parsed
     * @return a new instance of RouteTemplate
     */
    public static RouteTemplate of(String routeString) {
        ArrayList<TemplateSegment> templateSegments = new ArrayList<>();
        String[] segments = RouterUtils.getValidSegments(routeString);
        int parameterSegments = 0;
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            if (isParameterSegment(segment)) {
                templateSegments.add(new ParameterTemplateSegment(
                        getParameterName(segment)
                ));

                parameterSegments++;
            } else {
                templateSegments.add(new ConstantTemplateSegment(segment));
            }
        }

        return new RouteTemplate(templateSegments, parameterSegments);
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
        return segment.startsWith("{") && segment.endsWith("}")
                && segment.length() > 2;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }

    private abstract static class TemplateSegment {

        protected String segment;

        protected TemplateSegment(String segment) {
            this.segment = segment;
        }

        public abstract boolean match(String segment);

        @Override
        public String toString() {
            return segment;
        }
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

        protected ParameterTemplateSegment(String segment) {
            super(segment);
        }

        @Override
        public boolean match(String segment) {
            return true;
        }

        @Override
        public String toString() {
            return "{" + segment + "}";
        }

    }

}
