package pt.isel.ls.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

public class RouteTemplate {

    private final ArrayList<TemplateSegment> templateSegments;

    private RouteTemplate(ArrayList<TemplateSegment> templateSegments) {
        this.templateSegments = templateSegments;
    }

    public Optional<RequestParameters<String>> match(Path path) {
        HashMap<String, String> pathParameters = new HashMap<>();
        Iterator<TemplateSegment> templateSegmentIterator = templateSegments.iterator();
        for (String segment : path.getPathSegments()) {
            if (templateSegmentIterator.hasNext()) {
                TemplateSegment templateSegment = templateSegmentIterator.next();
                if (templateSegment.match(segment)) {
                    if (templateSegment instanceof ParameterTemplateSegment) {
                        // add the parameter to the map
                        pathParameters.put(((ParameterTemplateSegment) templateSegment).segment, segment);
                    }
                } else {
                    return Optional.empty();
                }
            }
        }

        // check if there are any obligatory parameters ahead
        while (templateSegmentIterator.hasNext()) {
            TemplateSegment templateSegment = templateSegmentIterator.next();
            if (templateSegment instanceof ParameterTemplateSegment) {
                if (((ParameterTemplateSegment) templateSegment).isObligatory) {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(new RequestParameters<>(pathParameters));
    }

    public static RouteTemplate of(String routeString) {
        ArrayList<TemplateSegment> templateSegments = new ArrayList<>();
        String[] segments = RouterUtils.getValidSegments(routeString);
        for (String segment : segments) {
            if (isParameterSegment(segment)) {
                templateSegments.add(new ParameterTemplateSegment(
                        getParameterName(segment),
                        !isOptionalParameter(segment)
                ));
            } else {
                templateSegments.add(new ConstantTemplateSegment(segment));
            }
        }

        return new RouteTemplate(templateSegments);
    }

    private static String getParameterName(String segment) {
        return segment.substring(1, segment.indexOf('}'));
    }

    private static boolean isParameterSegment(String segment) {
        return segment.startsWith("{") && segment.endsWith("}") && segment.length() > 2;
    }

    private static boolean isOptionalParameter(String segment) {
        return segment.endsWith("}?");
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
