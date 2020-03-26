package pt.isel.ls.router;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RouteRequest {

    private final Method method;
    private final Path path;
    private final HashMap<String, List<Parameter>> parameters;

    private HashMap<String, Parameter> pathParameters = new HashMap<>();

    /**
     * Creates a new instance of RouteRequest, representing a new request
     * @param method method used on the request
     * @param path path of the request
     * @param parameters parameters of the request
     */
    private RouteRequest(Method method, Path path, HashMap<String, List<Parameter>> parameters) {
        this.method = method;
        this.path = path;
        this.parameters = parameters;
    }

    /**
     * Gets a mandatory path parameter
     * If the parameter does not exist it throws an exception
     * @param parameterName name of the parameter
     * @return parameter string value
     * @throws ParameterNotFoundException in case the parameter does not exist
     */
    public Parameter getPathParameter(String parameterName) throws ParameterNotFoundException {
        return getOptionalPathParameter(parameterName)
                .orElseThrow(() -> new ParameterNotFoundException(parameterName));
    }

    /**
     * Gets an optional path parameter
     * @param parameterName name of the parameter
     * @return parameter string value
     */
    public Optional<Parameter> getOptionalPathParameter(String parameterName) {
        return Optional.ofNullable(pathParameters.get(parameterName));
    }

    /**
     * Gets a mandatory parameter
     * If the parameter does not exist it throws an exception
     * @param parameterName name of the parameter
     * @return parameter string value
     * @throws ParameterNotFoundException in case the parameter does not exist
     */
    public List<Parameter> getParameter(String parameterName) throws ParameterNotFoundException {
        return getOptionalParameter(parameterName)
                .orElseThrow(() -> new ParameterNotFoundException(parameterName));
    }

    /**
     * Gets an optional parameter
     * @param parameterName name of the parameter
     * @return parameter string value
     */
    public Optional<List<Parameter>> getOptionalParameter(String parameterName) {
        return Optional.ofNullable(parameters.get(parameterName));
    }

    /**
     * Sets the path parameters
     * This method should be used by the Router when the path
     * parameters become available
     * @param pathParameters path parameters map
     */
    public void setPathParameters(HashMap<String, Parameter> pathParameters) {
        this.pathParameters = pathParameters;
    }

    /**
     * Gets this Request path
     * @return request path
     */
    public Path getPath() {
        return path;
    }

    /**
     * Gets this request method
     * @return request method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Parses a string request into a new RouteRequest instance
     * @param request request to be parsed
     * @return a new instance of RouteRequest
     * @throws RouteException if an error occurs while parsing
     */
    public static RouteRequest of(String request) throws RouteException {
        try {
            String[] requestParts = request.split(" ");
            if (requestParts.length < 2) {
                throw new RouteException("Path not valid!");
            }

            Method method = Method.valueOf(requestParts[0]);
            Optional<Path> path = Path.of(requestParts[1]);
            if (path.isEmpty()) {
                throw new RouteException("Path not valid!");
            }

            HashMap<String, List<Parameter>> parameters;
            if (requestParts.length > 2) {
                parameters = parseParameters(requestParts[2]);
            } else {
                parameters = new HashMap<>();
            }

            return new RouteRequest(method, path.get(), parameters);
        } catch (RouteException routeException) {
            throw new RouteRequestParsingException(routeException.getMessage());
        }
    }

    /**
     * Parse the parameters of a parameter section
     * @param parameterSection parameter section to be parsed
     * @return parameter map
     * @throws RouteException if an error occurs
     */
    private static HashMap<String, List<Parameter>> parseParameters(String parameterSection)
            throws RouteException {

        parameterSection = URLDecoder.decode(parameterSection, StandardCharsets.UTF_8);
        String[] keyValueSections = parameterSection.split("&");
        HashMap<String, List<Parameter>> parameters = new HashMap<>();
        for (String keyValue : keyValueSections) {
            String[] kvSplit = keyValue.split("=");
            if (kvSplit.length != 2) {
                throw new RouteException("Error parsing route parameters!");
            }

            String key = kvSplit[0];
            String value = kvSplit[1];
            if (key.isBlank() || value.isBlank()) {
                throw new RouteException("Parameter key or/and value is/are blank");
            }

            List<Parameter> values = Optional.ofNullable(parameters.get(key))
                    .orElse(new ArrayList<>());

            values.add(new Parameter(value));
            parameters.put(key, values);
        }

        return parameters;
    }

    public static class ParameterNotFoundException extends RouteException {

        private ParameterNotFoundException(String parameterName) {
            super("Parameter " + parameterName + " not found!");
        }

    }

    public static class RouteRequestParsingException extends RouteException {

        private RouteRequestParsingException(String message) {
            super("Error while parsing the route: " + message);
        }

    }

}
