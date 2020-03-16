package pt.isel.ls.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RouteRequest {

    private final Method method;
    private final Path path;
    private final HashMap<String, List<String>> parameters;

    private HashMap<String, String> pathParameters = new HashMap<>();

    /**
     * Creates a new instance of RouteRequest, representing a new request
     * @param method method used on the request
     * @param path path of the request
     * @param parameters parameters of the request
     */
    private RouteRequest(Method method, Path path, HashMap<String, List<String>> parameters) {
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
    public String getPathParameter(String parameterName) throws ParameterNotFoundException {
        return getOptionalPathParameter(parameterName)
                .orElseThrow(() -> new ParameterNotFoundException(parameterName));
    }

    /**
     * Gets an optional path parameter
     * @param parameterName name of the parameter
     * @return parameter string value
     */
    public Optional<String> getOptionalPathParameter(String parameterName) {
        return Optional.ofNullable(pathParameters.get(parameterName));
    }

    /**
     * Gets a mandatory parameter
     * If the parameter does not exist it throws an exception
     * @param parameterName name of the parameter
     * @return parameter string value
     * @throws ParameterNotFoundException in case the parameter does not exist
     */
    public List<String> getParameter(String parameterName) throws ParameterNotFoundException {
        return getOptionalParameter(parameterName)
                .orElseThrow(() -> new ParameterNotFoundException(parameterName));
    }

    /**
     * Gets an optional parameter
     * @param parameterName name of the parameter
     * @return parameter string value
     */
    public Optional<List<String>> getOptionalParameter(String parameterName) {
        return Optional.ofNullable(parameters.get(parameterName));
    }

    /**
     * Sets the path parameters
     * This method should be used by the Router when the path
     * parameters become available
     * @param pathParameters path parameters map
     * @return the modified instance
     */
    public RouteRequest setPathParameters(HashMap<String, String> pathParameters) {
        this.pathParameters = pathParameters;
        return this;
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
     * Must only be used by the Router
     * @param request request to be parsed
     * @return a new instance of RouteRequest
     * @throws RouteRequestParsingException if an error occurs while parsing
     */
    static RouteRequest of(String request) throws RouteRequestParsingException {
        try {
            String[] requestParts = request.split(" ");
            Method method = Method.valueOf(requestParts[0]);
            Optional<Path> path = Path.of(requestParts[1]);
            if (path.isEmpty()) {
                throw new Exception("Path not valid!");
            }

            HashMap<String, List<String>> parameters;
            if (requestParts.length > 2) {
                parameters = parseParameters(requestParts[2]);
            } else {
                parameters = new HashMap<>();
            }

            return new RouteRequest(method, path.get(), parameters);
        } catch (Throwable throwable) {
            throw new RouteRequestParsingException(throwable.getMessage());
        }
    }

    /**
     * Parse the parameters of a parameter section
     * @param parameterSection parameter section to be parsed
     * @return parameter map
     * @throws ArrayIndexOutOfBoundsException if an error occurs
     */
    private static HashMap<String, List<String>> parseParameters(String parameterSection)
            throws ArrayIndexOutOfBoundsException {
        String[] keyValueSections = parameterSection.split("&");
        HashMap<String, List<String>> parameters = new HashMap<>();
        for (String keyValue : keyValueSections) {
            String[] kvSplit = keyValue.split("=");
            String key = kvSplit[0];
            String value = kvSplit[1];

            List<String> values = Optional.ofNullable(parameters.get(key))
                    .orElse(new ArrayList<>());

            values.add(value);
            parameters.put(key, values);
        }

        return parameters;
    }

    public static class ParameterNotFoundException extends Exception {

        private ParameterNotFoundException(String parameterName) {
            super("Parameter " + parameterName + " not found!");
        }

    }

    public static class RouteRequestParsingException extends Exception {

        private RouteRequestParsingException(String message) {
            super("Error while parsing the route: " + message);
        }

    }

}
